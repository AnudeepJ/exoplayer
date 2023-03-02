package com.example.myapplication.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download.STATE_COMPLETED
import androidx.media3.exoplayer.offline.Download.STATE_DOWNLOADING
import androidx.media3.exoplayer.offline.Download.STATE_QUEUED
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemVideoBinding

@UnstableApi class VideoUrlAdapter(private val videoUrlList: List<MediaItemDto>) :
    RecyclerView.Adapter<VideoUrlAdapter.VideoListViewHolder>() {

    interface ItemClick {
        fun onItemClick(id: Int)
        fun onDownloadClick(id: Int)
    }

    lateinit var itemClickListner: ItemClick

    fun setListner(itemClick: ItemClick) {
        this.itemClickListner = itemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
        val videoBinding =
            ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoListViewHolder(videoBinding, itemClickListner)
    }

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        holder.view.video.text = videoUrlList[position].url
        holder.onBind(videoUrlList[position])
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
            val downloadState = when (item.state) {
                STATE_COMPLETED -> "STATE_COMPLETED"
                STATE_DOWNLOADING -> "STATE_DOWNLOADING"
                STATE_QUEUED -> "STATE_QUEUED"
                else -> ""
            }
            view.state.text = downloadState
            Log.d("PRS", "on click")
        }
    }
}