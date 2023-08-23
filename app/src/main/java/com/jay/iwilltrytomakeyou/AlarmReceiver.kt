package com.jay.iwilltrytomakeyou

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat


class AlarmReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
            val alarmId = intent.getLongExtra("extra_alarm_id", -1)
//        notification manager for normal alarm

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "alarm_channel"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val channel = NotificationChannel(
                    channelId,
                    "Alarm Channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }
            val uniqueNotificationId = NOTIFICATION_ID_OFFSET + alarmId.toInt()
            val notificationBuilder = NotificationCompat.Builder(context, "alarm_channel")
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle("Yo Alarm.")
                .setContentText("Tnx")
                .build()


            notificationManager.notify(uniqueNotificationId, notificationBuilder)
        }

        companion object {
            private const val NOTIFICATION_ID_OFFSET = 1000
        }
}


