package com.example.myapplication.ui.main

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.offline.DownloadManager
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@UnstableApi
object JSMDownloadManager {

    private var downloadManager: DownloadManager? = null

    fun getDownloadManager(context: Context): DownloadManager {
        if (downloadManager == null) {
            val databaseprovider = StandaloneDatabaseProvider(context)
            val downloadExecutor = Executors.newFixedThreadPool(3)
            downloadManager = MediaCache.getCache(context.applicationContext)?.let {
                DownloadManager(
                    context.applicationContext,
                    databaseprovider,
                    it,
                    DefaultHttpDataSource.Factory(),
                    downloadExecutor
                )
            }
        }

        return downloadManager!!
    }
}