package com.jay.iwilltrytomakeyou

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class BootReceiver: BroadcastReceiver()
{

    override fun onReceive(context: Context?, intent: Intent?)
    {
//        this condition check that if user restarted the phone that this condition execute

        if (intent?.action==Intent.ACTION_BOOT_COMPLETED)
        {
            val alarmId = intent.getLongExtra("alarm_id", -1)
            val channelId = "alarm_channel"
            val notificationBuilder = context?.let{
                NotificationCompat.Builder(it, channelId)
                    .setSmallIcon(R.drawable.alarm)
                    .setContentTitle("Alarm After Boot")
                    .setContentText("Your alarm notification after boot.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
            }
            val uniqueNotificationId = alarmId.toInt()

            if (notificationBuilder != null)
            {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                )
                {

                    return
                }
                context.let { NotificationManagerCompat.from(it) }

                    .notify(uniqueNotificationId, notificationBuilder.build())
            }
        }
    }

}
