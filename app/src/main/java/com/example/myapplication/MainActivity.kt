package com.example.myapplication

import android.app.AppOpsManager
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.DownloadService
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.main.BundleConstants.IS_VIDEO_OFFLINE
import com.example.myapplication.ui.main.BundleConstants.VIDEO_DOWNLOAD
import com.example.myapplication.ui.main.MainFragment
import com.example.myapplication.ui.main.VideoDetailFragment

@UnstableApi
class MainActivity : AppCompatActivity() {

    var isOfflineVideo = false
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        val bundle = intent.extras
        isOfflineVideo = bundle!!.getBoolean("IS_OFFLINE")
        val data = Bundle()
        data.putBoolean("IS_OFFLINE", isOfflineVideo)
        if (savedInstanceState == null) {
            val fragment = MainFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
            fragment.arguments = data
        }
        startDownloadService()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    fun replaceFragment(url: String, state: Int) {
        val bundle = Bundle()
        bundle.putString("URL", url)
        bundle.putInt(VIDEO_DOWNLOAD, state)
        bundle.putBoolean(IS_VIDEO_OFFLINE, isOfflineVideo)
        val fragment = VideoDetailFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) supportFragmentManager.popBackStack() else super.onBackPressed()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isOfflineVideo) {
            return false
        } else {
            menuInflater.inflate(R.menu.menu_item, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val item = item.itemId
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        when (item) {
            R.id.pause -> {
                if (fragment is MainFragment)
                    fragment.pauseDownloads()
                return true
            }
            R.id.resume -> {
                if (fragment is MainFragment)
                    fragment.resumeDownloads()
                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> {
                return false
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        supportActionBar?.hide()
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val status = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                android.os.Process.myUid(),
                packageName
            ) ==
                    AppOpsManager.MODE_ALLOWED
        } else {
            false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (status) {
                enterPIPMode()
            } else {
                val intent = Intent(
                    "android.settings.PICTURE_IN_PICTURE_SETTINGS",
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
        } else {
            Toast.makeText(this, "Feature Not Supported!!", Toast.LENGTH_SHORT).show()

        }

    }

    //For N devices that support it, not "officially"
    @Suppress("DEPRECATION")
    fun enterPIPMode() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is VideoDetailFragment)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && packageManager
                    .hasSystemFeature(
                        PackageManager.FEATURE_PICTURE_IN_PICTURE
                    )
            ) {
                fragment.binding.videoDetail.useController = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val params = PictureInPictureParams.Builder()
                    this.enterPictureInPictureMode(params.build())
                } else {
                    this.enterPictureInPictureMode()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is VideoDetailFragment) {
            fragment.binding.videoDetail.useController = !isInPictureInPictureMode
            if (isInPictureInPictureMode)
                supportActionBar?.hide()
            else
                supportActionBar?.show()
        }

    }

}