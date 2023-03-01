package com.example.myapplication.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainBinding

class MainFragment : Fragment(), VideoUrlAdapter.ItemClick {

    companion object {
        fun newInstance() = MainFragment()
    }

    lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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
    }

    private fun initRecyclerView() {
        binding.videoUrl.layoutManager = LinearLayoutManager(requireContext())
        val videoAdapter = VideoUrlAdapter(viewModel.urlList)
        videoAdapter.setListner(this)
        binding.videoUrl.adapter = videoAdapter
    }

    override fun onItemClick(videoUrl: String) {
        Log.d("PRS","Main fragment")
        if(activity is MainActivity) {
            (activity as MainActivity).replaceFragment(videoUrl)
        }
    }

}