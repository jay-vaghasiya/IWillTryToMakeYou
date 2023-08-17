package com.jay.iwilltrytomakeyou

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.jay.iwilltrytomakeyou.database.Alarm
import java.util.Calendar

class AlarmReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmId = intent?.getLongExtra("alarm_id", -1)

        if (intent != null) {
            if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

                val alarmDataList = mutableListOf<Alarm>()

                for (alarmData in alarmDataList) {
                    val alarmId = System.currentTimeMillis()
                    val alarmDateTime = Calendar.getInstance()

                    context?.let { AlarmManager(it) }?.scheduleAlarm(alarmId, alarmDateTime)

                }
            }

        }


        if (alarmId != null && alarmId != -1L) {
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val channel = NotificationChannel(
                    "alarm_channel",
                    "Alarm Channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }

            val notificationBuilder = NotificationCompat.Builder(context, "alarm_channel")
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle("Yo Alarm . Turn it off or Snooze")
                .setContentText("Tnx to turn off Alarm")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            val notification = notificationBuilder.build()
            notificationManager.notify(alarmId.toInt(), notification)
        }
    }

}