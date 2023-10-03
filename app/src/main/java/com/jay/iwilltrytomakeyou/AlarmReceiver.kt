package com.jay.iwilltrytomakeyou

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getLongExtra(AlarmManager.EXTRA_ALARM_ID, -1)
        val label = intent.getStringExtra("name")



        val mChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "alarm", // Provide a meaningful channel name
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.alarm)
            .setContentTitle(label)
            .setContentText("Alarm Triggered")
            .setSound(ringtoneManager)
            .build()

        notificationManager.createNotificationChannel(mChannel)
        notificationManager.notify(alarmId.toInt() + NOTIFICATION_ID_OFFSET, notificationBuilder)
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "1"
        private const val NOTIFICATION_ID_OFFSET = 1000

    }
}

