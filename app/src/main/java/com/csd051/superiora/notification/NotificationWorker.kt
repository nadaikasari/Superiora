package com.csd051.superiora.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.di.Injection
import com.csd051.superiora.ui.home.home.HomeActivity
import com.csd051.superiora.utils.NOTIFICATION_CHANNEL_ID

class NotificationWorker (private val ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val channelName = inputData.getString(NOTIFICATION_CHANNEL_ID)
    private val notificationId = 1

    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(applicationContext, HomeActivity::class.java).apply {
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun doWork(): Result {
        val prefManager = androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val shouldNotify = prefManager.getBoolean(applicationContext.getString(R.string.pref_key_notify), false)


        val repo = Injection.provideRepository(ctx)

        val notificationStyle = NotificationCompat.InboxStyle()
        repo.getTodayNotification().forEach {
            notificationStyle.addLine(it.title)
        }


        if (shouldNotify) {

            val pendingIntent = getPendingIntent()
            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setStyle(notificationStyle)
                .setContentIntent(pendingIntent)
                .setContentTitle("Your Tasks Today")
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH)
                notification.setChannelId(NOTIFICATION_CHANNEL_ID)
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify( notificationId, notification.build())
        }

        return Result.success()
    }

}
