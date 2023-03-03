package com.example.myapplication.ui.main

import android.graphics.Color
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.DemoDownloadService
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainBinding
import com.example.myapplication.databinding.ItemVideoBinding

const val TAG = "Exoplayer"

@UnstableApi
class MainFragment : Fragment(), VideoUrlAdapter.ItemClick {

    companion object {
        fun newInstance() = MainFragment()
    }

    lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        JSMDownloadManager.getDownloadManager(requireContext())
            .addListener(object : DownloadManager.Listener {
                override fun onDownloadChanged(
                    downloadManager: DownloadManager,
                    download: Download,
                    finalException: java.lang.Exception?
                ) {
                    Log.d(TAG, "onDownloadChanged: ${download.state}")
                    Log.d(TAG, "onDownloadChanged:  download.request.id : ${download.request.id}")
                    Log.d(TAG, "onDownloadChanged: Uri  ${download.request.uri}")

                    viewModel.updateItem(download)
                    val itemPosition = download.request.id.toInt() - 1
                    binding.videoUrl.adapter?.notifyItemChanged(itemPosition)
                    super.onDownloadChanged(downloadManager, download, finalException)
                }
            })
    }



    private fun initRecyclerView() {
        binding.videoUrl.layoutManager = LinearLayoutManager(requireContext())
        val videoAdapter = VideoUrlAdapter(viewModel.urlList, context)
        videoAdapter.setListner(this)
        binding.videoUrl.adapter = videoAdapter
    }

    override fun onItemClick(id: Int) {
        Log.d("PRS", "Main fragment")
        if (activity is MainActivity) {
            val download =
                JSMDownloadManager.getDownloadManager(requireContext()).downloadIndex.getDownload(id.toString())
            Log.d(TAG, "onItemClick: ${download?.state}")
            val url = viewModel.getUrlForID(id)
            (activity as MainActivity).replaceFragment(url, download?.state ?: 0)
        }

    }

    override fun onDownloadClick(id: Int) {
        val url = viewModel.getUrlForID(id)
        val downloadRequest = DownloadRequest.Builder(id.toString(), Uri.parse(url)).build()
//        downloadRequest.toMediaItem()
        DownloadService.sendAddDownload(
            requireContext(),
            DemoDownloadService::class.java, downloadRequest, false
        )

    }

    override fun removeDownload(id: Int) {
        JSMDownloadManager.getDownloadManager(requireContext()).removeDownload(id.toString())
        binding.videoUrl.adapter?.notifyItemChanged(id - 1)
    }

    override fun onMenuClick(view: View, position: Int, id: Int) {
        val popup = PopupMenu(view.context,view)
        popup.inflate(R.menu.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.remove_from_device -> removeDownload(id)
            }
            true
        }
        popup.show()
    }


}