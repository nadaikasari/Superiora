package com.csd051.superiora.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.csd051.superiora.R
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.Task
import com.csd051.superiora.di.Injection
import com.csd051.superiora.ui.home.home.HomeActivity
import com.csd051.superiora.utils.*
import java.util.*

class DailyReminder: BroadcastReceiver() {

    private lateinit var alarmIntent: PendingIntent

    //todo ini juga minta context tapi bingung
    override fun onReceive(context: Context, intent: Intent) {
        executeThread {
            val repository = Injection.provideRepository(context)
            val data = repository.getTodayNotification()

            data.let {
                if (it.isNotEmpty()) showNotification(context, it)
            }
        }
    }

    fun setDailyReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, DailyReminder::class.java).let { intent ->
            PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        }
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
        Toast.makeText(context, "Notifikasi Pengingat dinyalakan", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, DailyReminder::class.java).let { intent ->
            PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        }
        alarmManager.cancel(alarmIntent)
        Toast.makeText(context, "Notifikasi Pengingat dimatikan", Toast.LENGTH_SHORT).show()
    }

    private fun showNotification(context: Context, content: List<Task>) {
        val notificationStyle = NotificationCompat.InboxStyle()
        val timeString = context.resources.getString(R.string.notification_message_format)
        content.forEach {
            val courseData = String.format(timeString, it.title)
            notificationStyle.addLine(courseData)
        }

        val prefManager = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        val shouldNotify =
            prefManager.getBoolean(context.getString(R.string.pref_key_notify), false)

        if (shouldNotify) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification: NotificationCompat.Builder =
                NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setStyle(notificationStyle)
                    .setContentIntent(getPendingIntent(context))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notification.setChannelId(NOTIFICATION_CHANNEL_ID)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(NOTIFICATION_ID, notification.build())
        }
    }

    private fun getPendingIntent(context: Context): PendingIntent? {
        val intent = Intent(context, HomeActivity::class.java)

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
}