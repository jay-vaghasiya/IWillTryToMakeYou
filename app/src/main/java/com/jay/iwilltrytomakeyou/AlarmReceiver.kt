package com.jay.iwilltrytomakeyou

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val alarmId = intent.getLongExtra("alarm_id", -1)
        val label = intent.getStringExtra("name")
        val pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_IMMUTABLE)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val ringtoneManager=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.alarm)
            .setContentTitle(label)
            .setContentText("Alarm Triggered")
            .setContentIntent(pendingIntent)
            .setSound(ringtoneManager)
            .build()

        notificationManager.notify(alarmId.toInt() + NOTIFICATION_ID_OFFSET, notificationBuilder)
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "alarm_channel"
        private const val NOTIFICATION_ID_OFFSET = 1000

    }
}

