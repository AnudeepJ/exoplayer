package com.example.myapplication.ui.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.myapplication.databinding.VideoDetailBinding
import java.io.File

@UnstableApi
class VideoDetailFragment : Fragment() {
    var url = ""
    var permission = arrayOf("android.permission.READ_EXTERNAL_STORAGE")
    private var player: ExoPlayer? = null

    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    lateinit var simpleCache: SimpleCache

    var downloadRequest: DownloadRequest? = null
    var isFromOffline = false
    lateinit var binding: VideoDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestPermissions(permission, 80)
        }
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VideoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url = arguments?.getString("URL", "") ?: ""
        Log.d("PRS", "url " + url)
        initializePlayerFromURI2()
    }

    fun initializePlayerFromURI2() {
        //releasePlayer()


        player = ExoPlayer.Builder(requireContext())
            .build()
            .also { exoPlayer ->
                binding.videoDetail.player = exoPlayer
                val mediaItem = MediaItem.fromUri(url)
                //val mediaItem = downloadRequest?.let { MediaItem.fromUri(it.uri) }
                val mediaSource = ProgressiveMediaSource.Factory(buildCacheDataSourceFactory())
                    .createMediaSource(mediaItem)
                // val mediaSource = SingleSampleMediaSource.Factory(buildCacheDataSourceFactory()).createMediaSource(mediaItem)
                // uncomment below line for normal url video
                //exoPlayer.setMediaItem(mediaItem)

                exoPlayer.setMediaSource(mediaSource)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentItem, playbackPosition)
                exoPlayer.prepare()
            }

    }

    fun buildCacheDataSourceFactory(): DataSource.Factory {
        val cache = getDownloadCache()
        val cacheSink = CacheDataSink.Factory()
            .setCache(cache)
        val upstreamFactory =
            DefaultDataSource.Factory(requireContext(), DefaultHttpDataSource.Factory())
        return CacheDataSource.Factory()
            .setCache(cache)
            .setCacheWriteDataSinkFactory(cacheSink)
            .setCacheReadDataSourceFactory(FileDataSource.Factory())
            .setUpstreamDataSourceFactory(upstreamFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    @Synchronized
    private fun getDownloadCache(): Cache {
        return MediaCache.getCache(requireContext())!!
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
        getDownloadCache().release()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

}
