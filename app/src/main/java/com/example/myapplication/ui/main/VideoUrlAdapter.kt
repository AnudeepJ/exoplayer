package com.example.myapplication.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemVideoBinding

class VideoUrlAdapter(private val videoUrlList: List<MediaItemDto>) :
    RecyclerView.Adapter<VideoUrlAdapter.VideoListViewHolder>() {

    interface ItemClick {
        fun onItemClick(videoUrl: String)
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
        holder.onBind(videoUrlList[position].url)
    }

    override fun getItemCount(): Int {
        return videoUrlList.size
    }


    class VideoListViewHolder(val view: ItemVideoBinding, val itemClickListner: ItemClick) :
        RecyclerView.ViewHolder(view.root) {
        fun onBind(videoUrl: String) {
            view.video.setOnClickListener {
                itemClickListner.onItemClick(videoUrl)
            }
            view.downloadButton.setOnClickListener {
                itemClickListner.onDownloadClick(bindingAdapterPosition)
            }
            Log.d("PRS", "on click")
        }
    }
}