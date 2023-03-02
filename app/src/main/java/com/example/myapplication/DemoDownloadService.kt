package com.example.myapplication


import android.R
import android.app.Notification
import android.content.Context
import android.util.Log
import androidx.media3.common.util.NotificationUtil
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.PlatformScheduler
import androidx.media3.exoplayer.scheduler.Requirements.RequirementFlags
import androidx.media3.exoplayer.scheduler.Scheduler
import com.example.myapplication.ui.main.MediaCache
import com.example.myapplication.ui.main.TAG
import java.io.File
import java.util.concurrent.Executor


@UnstableApi
class DemoDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    "download_channel",
    R.string.unknownName,  /* channelDescriptionResourceId= */
    0
) {
    private val DOWNLOAD_CONTENT_DIRECTORY = ""
    private var downloadCache: Cache? = null
    private var downloadNotificationHelper: DownloadNotificationHelper? = null

    override fun getDownloadManager(): DownloadManager {
        // This will only happen once, because getDownloadManager is guaranteed to be called only once
        // in the life cycle of the process.
        val downloadManager: DownloadManager = getdownloadManager()
//        val downloadNotificationHelper: DownloadNotificationHelper =
//            getDownloadNotificationHelper(context = this)!!
        downloadManager?.addListener(
            object : DownloadManager.Listener {
                override fun onDownloadChanged(
                    downloadManager: DownloadManager,
                    download: Download,
                    finalException: java.lang.Exception?
                ) {
                    Log.d(TAG, "onDownloadChanged: ${download.state}")
                    Log.d(TAG, "onDownloadChanged:  download.request.id : ${download.request.id}")
                    Log.d(TAG, "onDownloadChanged: Uri  ${download.request.uri}")


                    super.onDownloadChanged(downloadManager, download, finalException)
                }
            }
        )
        return downloadManager
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
                R.drawable.sym_def_app_icon,  /* contentIntent= */
                null,  /* message= */
                null,
                downloads,
                notMetRequirements
            )
    }


    /**
     * Creates and displays notifications for downloads when they complete or fail.
     *
     *
     * This helper will outlive the lifespan of a single instance of [DemoDownloadService].
     * It is static to avoid leaking the first [DemoDownloadService] instance.
     */
    private class TerminalStateNotificationHelper(
        context: Context, notificationHelper: DownloadNotificationHelper, firstNotificationId: Int
    ) :
        DownloadManager.Listener {
        private val context: Context
        private val notificationHelper: DownloadNotificationHelper
        private var nextNotificationId: Int


        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?
        ) {
            Log.d(TAG, "onDownloadChanged: ${download.state}")
            if (download.state == Download.STATE_COMPLETED) {
                Log.d("PRS", "Download complete")
            }
            val notification: Notification
            notification = if (download.state == Download.STATE_COMPLETED) {
                notificationHelper.buildDownloadCompletedNotification(
                    context,
                    R.drawable.sym_def_app_icon, /* contentIntent= */
                    null,
                    Util.fromUtf8Bytes(download.request.data)
                )
            } else if (download.state == Download.STATE_FAILED) {
                notificationHelper.buildDownloadFailedNotification(
                    context,
                    R.drawable.sym_def_app_icon,  /* contentIntent= */
                    null,
                    Util.fromUtf8Bytes(download.request.data)
                )
            } else {
                return
            }
            NotificationUtil.setNotification(context, nextNotificationId++, notification)
        }

        init {
            this.context = context.getApplicationContext()
            this.notificationHelper = notificationHelper
            nextNotificationId = firstNotificationId
        }
    }

    companion object {
        private const val JOB_ID = 1
        private const val FOREGROUND_NOTIFICATION_ID = 1
    }


    fun getdownloadManager(): DownloadManager {

        val databaseprovider = StandaloneDatabaseProvider(applicationContext)
        val downloadExecutor = Executor { obj: Runnable -> obj.run() }
        val downloadContentDirectory = File(
            getExternalFilesDir(null),
            "files"
        )


        val downloadManager = MediaCache.getCache(applicationContext)?.let {
            DownloadManager(
                applicationContext,
                databaseprovider,
                it,
                DefaultHttpDataSource.Factory(),
                downloadExecutor
            )
        }

        return downloadManager!!
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