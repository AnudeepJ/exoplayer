package com.example.myapplication


import android.app.Notification
import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.cache.Cache
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.PlatformScheduler
import androidx.media3.exoplayer.scheduler.Requirements.RequirementFlags
import androidx.media3.exoplayer.scheduler.Scheduler
import com.example.myapplication.ui.main.JSMDownloadManager


@UnstableApi
class DemoDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    "download_channel",
    androidx.media3.exoplayer.R.string.exo_download_notification_channel_name,  /* channelDescriptionResourceId= */
    0
) {
    private val DOWNLOAD_CONTENT_DIRECTORY = ""
    private var downloadCache: Cache? = null
    private var downloadNotificationHelper: DownloadNotificationHelper? = null

    override fun getDownloadManager(): DownloadManager {
        // This will only happen once, because getDownloadManager is guaranteed to be called only once
        // in the life cycle of the process.
        return JSMDownloadManager.getDownloadManager(applicationContext)
    }

    override fun getScheduler(): Scheduler? {
        return if (Util.SDK_INT >= 21) PlatformScheduler(this, JOB_ID) else null
    }

    override fun getForegroundNotification(
        downloads: List<Download>, notMetRequirements: @RequirementFlags Int
    ): Notification {
        return getDownloadNotificationHelper( /* context= */this)!!
            .buildProgressNotification( /* context= */
                this,
                R.drawable.ic_download,  /* contentIntent= */
                null,  /* message= */
                null,
                downloads,
                notMetRequirements
            )
    }


    companion object {
        private const val JOB_ID = 1
        private const val FOREGROUND_NOTIFICATION_ID = 1
    }


    @Synchronized
    fun getDownloadNotificationHelper(
        context: Context?
    ): DownloadNotificationHelper? {
        if (downloadNotificationHelper == null) {
            downloadNotificationHelper =
                DownloadNotificationHelper(
                    context!!,
                    "download_channel"
                )
        }
        return downloadNotificationHelper
    }
}