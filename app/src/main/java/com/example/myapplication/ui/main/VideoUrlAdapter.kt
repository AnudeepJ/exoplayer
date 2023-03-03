package com.example.myapplication.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download.STATE_COMPLETED
import androidx.media3.exoplayer.offline.Download.STATE_DOWNLOADING
import androidx.media3.exoplayer.offline.Download.STATE_QUEUED
import androidx.media3.exoplayer.offline.Download.STATE_REMOVING
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemVideoBinding

@UnstableApi
class VideoUrlAdapter(private val videoUrlList: List<MediaItemDto>, val context: Context?) :
    RecyclerView.Adapter<VideoUrlAdapter.VideoListViewHolder>() {
    lateinit var videoBinding: ItemVideoBinding
    interface ItemClick {
        fun onItemClick(id: Int)
        fun onDownloadClick(id: Int)
        fun removeDownload(id:Int)

        fun onMenuClick(view:View,position:Int,id:Int)
    }

    lateinit var itemClickListner: ItemClick

    fun setListner(itemClick: ItemClick) {
        this.itemClickListner = itemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
         videoBinding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoListViewHolder(videoBinding, itemClickListner)
    }

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        holder.view.video.text = videoUrlList[position].title
        holder.onBind(videoUrlList[position])

        if (context != null) {
            Glide.with(context)
                .load(videoUrlList[position].imageUrl)
                .into(holder.view.videoThumbnail)
        }
    }

    override fun getItemCount(): Int {
        return videoUrlList.size
    }


    @UnstableApi
    class VideoListViewHolder(val view: ItemVideoBinding, val itemClickListner: ItemClick) :
        RecyclerView.ViewHolder(view.root) {
        fun onBind(item: MediaItemDto) {
            view.video.setOnClickListener {
                itemClickListner.onItemClick(item.id)
            }
            view.downloadButton.setOnClickListener {
                itemClickListner.onDownloadClick(item.id)
            }
            view.itemDownloadedButton.setOnClickListener {
                itemClickListner.onMenuClick(it,item.id-1,item.id)
            }
            when (item.state) {
                STATE_COMPLETED -> {
                    view.state.text = "DOWNLOAD_COMPLETED"
                    view.itemDownloadedButton.visibility = View.VISIBLE
                    view.progressbarDownload.visibility =View.GONE
                    view.itemDownloadingButton.visibility = View.GONE
                    view.itemDownloadButton.visibility = View.GONE
                }
                STATE_DOWNLOADING -> {
                    view.state.text = "DOWNLOAD_DOWNLOADING"
                    view.progressbarDownload.visibility =View.VISIBLE
                    view.itemDownloadingButton.visibility = View.VISIBLE
                    view.itemDownloadedButton.visibility = View.GONE
                    view.itemDownloadButton.visibility = View.GONE
                }
                STATE_QUEUED -> {
                    view.state.text = "DOWNLOAD_QUEUED"
                    view.itemDownloadButton.visibility = View.VISIBLE
                    view.progressbarDownload.visibility =View.GONE
                    view.itemDownloadingButton.visibility = View.GONE
                    view.itemDownloadedButton.visibility = View.GONE

                }
                STATE_REMOVING ->{
                    view.state.text = "DOWNLOAD_REMOVED"
                    view.itemDownloadButton.visibility = View.GONE
                    view.progressbarDownload.visibility =View.GONE
                    view.itemDownloadingButton.visibility = View.GONE
                    view.itemDownloadedButton.visibility = View.GONE
                }
                else -> ""
            }
        }
    }
}