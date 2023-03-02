package com.example.myapplication.ui.main

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

@UnstableApi
object MediaCache {
    private val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
    var downloadcache: Cache? = null

    fun getCache(context: Context): Cache? {
        if (downloadcache == null) {
            val downloadContentDirectory = File(
                context.getExternalFilesDir(null),
                DOWNLOAD_CONTENT_DIRECTORY
            )
            // val cacheEvictor = LeastRecentlyUsedCacheEvictor(10 * 1024 * 1024)
            downloadcache = SimpleCache(
                downloadContentDirectory, /*cacheEvictor*/
                NoOpCacheEvictor(),
                StandaloneDatabaseProvider(context)
            )
        }
        return downloadcache
    }
}