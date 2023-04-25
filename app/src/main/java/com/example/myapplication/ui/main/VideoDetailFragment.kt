package com.example.myapplication.ui.main

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.cronet.CronetDataSource
import androidx.media3.datasource.cronet.CronetUtil
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.myapplication.R
import com.example.myapplication.databinding.VideoDetailBinding
import com.example.myapplication.ui.main.BundleConstants.IS_VIDEO_OFFLINE
import org.chromium.net.CronetEngine
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.UnknownHostException
import java.util.concurrent.Executors


@UnstableApi
class VideoDetailFragment : Fragment(), Player.Listener {
    var url = ""
    private var state: Int = 0
    var permission = arrayOf("android.permission.READ_EXTERNAL_STORAGE")
    var player: ExoPlayer? = null

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
        state = arguments?.getInt(BundleConstants.VIDEO_DOWNLOAD) ?: 0
        isFromOffline = arguments?.getBoolean(IS_VIDEO_OFFLINE) ?: false
//      if(url == "https://objectstorage.ap-hyderabad-1.oraclecloud.com/n/ax8o1gwvyizc/b/bucket-exoplayer-demo/o/file_example_MP3_5MG.mp3"){
//          binding.videoDetail.videoSurfaceView = null
//      }

        Log.d("PRS", "url " + url)
        binding.videoDetail.setShowNextButton(false)
        binding.videoDetail.setShowPreviousButton(false)
        binding.videoDetail.setShowSubtitleButton(false)
        binding.videoDetail.setFullscreenButtonClickListener {
            if (it) {
                val layoutParams = binding.videoDetail.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                binding.videoDetail.layoutParams = layoutParams
                player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                (activity as AppCompatActivity).supportActionBar?.hide()

            } else {
                val layoutParams = binding.videoDetail.layoutParams
                val dimensionInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    200f,
                    resources.displayMetrics
                ).toInt()
                (activity as AppCompatActivity).supportActionBar?.show()

                layoutParams.height = dimensionInDp
                binding.videoDetail.layoutParams = layoutParams
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT

            }
            Log.d("FULL_SCREEN", it.toString())

        }
        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val layoutParams = binding.videoDetail.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding.videoDetail.layoutParams = layoutParams
            player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            Log.d("FULL_SCREEN", "${binding.videoDetail.resizeMode}  resizemode")
        } else {
            val layoutParams = binding.videoDetail.layoutParams
            val dimensionInDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                200f,
                resources.displayMetrics
            ).toInt()

            layoutParams.height = dimensionInDp
            binding.videoDetail.layoutParams = layoutParams
        }

        initializePlayerFromURI2()
    }

    fun initializePlayerFromURI2() {
        //releasePlayer()


        player = ExoPlayer.Builder(requireContext())
            .build()
            .also { exoPlayer ->
                binding.videoDetail.player = exoPlayer
                val mediaItem = MediaItem.fromUri(url)
                if (isFromOffline) {
                    exoPlayer.setMediaItem(mediaItem)
                } else {
                    //val mediaItem = downloadRequest?.let { MediaItem.fromUri(it.uri) }
                    val mediaSource = ProgressiveMediaSource.Factory(buildCacheDataSourceFactory())
                        .createMediaSource(mediaItem)
                    // val mediaSource = SingleSampleMediaSource.Factory(buildCacheDataSourceFactory()).createMediaSource(mediaItem)
                    // uncomment below line for normal url video
                    //exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.setMediaSource(mediaSource)
                }
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentItem, playbackPosition)
                exoPlayer.prepare()
            }

        player?.addListener(this)

    }

    fun buildCacheDataSourceFactory(): DataSource.Factory {
        val cache = MediaCache.getCache(requireContext())
        val upstreamFactory =
            DefaultDataSource.Factory(requireContext(), DefaultHttpDataSource.Factory())
        return if (state == 3) {
            CacheDataSource.Factory()
                .setCache(cache!!)
                .setCacheWriteDataSinkFactory(null)
                .setUpstreamDataSourceFactory(upstreamFactory)
        } else {
            val cacheSink = CacheDataSink.Factory()
                .setCache(cache!!)
            CacheDataSource.Factory()
                .setCache(cache)
                .setCacheWriteDataSinkFactory(cacheSink)
                .setCacheReadDataSourceFactory(FileDataSource.Factory())
                .setUpstreamDataSourceFactory(upstreamFactory)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        }
    }

    private fun getHttpDataSourceFactory(context: Context): HttpDataSource.Factory {
        val cronetEngine: CronetEngine? = CronetUtil.buildCronetEngine(context)
        var httpDataSourceFactory: HttpDataSource.Factory? = null

        if (cronetEngine != null) httpDataSourceFactory =
            CronetDataSource.Factory(cronetEngine, Executors.newSingleThreadExecutor())

        if (httpDataSourceFactory == null) {
            // We don't want to use Cronet, or we failed to instantiate a CronetEngine.
            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
            CookieHandler.setDefault(cookieManager)
            httpDataSourceFactory = DefaultHttpDataSource.Factory()
        }
        return httpDataSourceFactory
    }
    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
//        MediaCache.getCache(requireContext())?.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    var forceStop = false
    override fun onStop() {
        super.onStop()
        forceStop = true
        player?.pause()
    }

    override fun onStart() {
        if (forceStop) {
            player?.play()
        }
        super.onStart()
    }

    override fun onPlayerError(error: PlaybackException) {
        error.printStackTrace()
        if(error is ExoPlaybackException) {
            Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()
        }
        super.onPlayerError(error)
    }
}
