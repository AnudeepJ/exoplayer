package com.example.myapplication.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download

@UnstableApi
class MainViewModel : ViewModel() {
    var urlList = DataGenerator.createList()
    var offlineList = DataGenerator.createOfflineList()


    fun getDownloadStatus(id: String, context: Context): Int {
        val downloadManager = JSMDownloadManager.getDownloadManager(context)
        val download = downloadManager.downloadIndex.getDownload(id)
        return download?.state ?: 0
    }

    fun getUrlForID(id: Int): String {
        return urlList.filter { it.id == id }[0].url
    }

    fun getOfflineUrlForID(id: Int): String {
        return offlineList.filter { it.id == id }[0].url
    }

    fun updateItem(download: Download) {
        val item = urlList.filter { it.id == download.request.id.toInt() }[0]
        item.state = download.state
    }


}

