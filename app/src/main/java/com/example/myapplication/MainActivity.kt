package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.DownloadService
import com.example.myapplication.ui.main.BundleConstants.VIDEO_DOWNLOAD
import com.example.myapplication.ui.main.MainFragment
import com.example.myapplication.ui.main.VideoDetailFragment

@UnstableApi
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        startDownloadService()
    }

    fun replaceFragment(url: String, state: Int) {
        val bundle = Bundle()
        bundle.putString("URL", url)
        bundle.putInt(VIDEO_DOWNLOAD, state)
        val fragment = VideoDetailFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStack() else super.onBackPressed()
    }
    private fun startDownloadService() {
        // Starting the service in the foreground causes notification flicker if there is no scheduled
        // action. Starting it in the background throws an exception if the app is in the background too
        // (e.g. if device screen is locked).
        try {
            DownloadService.start(this, DemoDownloadService::class.java)
        } catch (e: IllegalStateException) {
            DownloadService.startForeground(this, DemoDownloadService::class.java)
        }
    }

}