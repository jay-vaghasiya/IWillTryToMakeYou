package com.jay.iwilltrytomakeyou

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.jay.iwilltrytomakeyou.database.Alarm
import java.util.Calendar

class AlarmManager(private val context: Context) {

    fun scheduleAlarm(alarmId: Long, alarmDataTime: Calendar, alarm: Alarm) {

        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "ALARM_TRIGGERED"
            putExtra("alarm_id", alarmId)
            putExtra("name", alarm.label)
        }

        val pendingIntent:PendingIntent =
            PendingIntent.getBroadcast(context, alarm.id.toInt(), alarmIntent, PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmDataTime.timeInMillis,
                    pendingIntent
                )


            else -> {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    alarmDataTime.timeInMillis,
                    pendingIntent
                )
            }
        }

        val receiver = ComponentName(context, BootReceiver::class.java)

        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    fun cancelAlarm(alarmId: Long, alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "ALARM_TRIGGERED" // Make sure to use the same action as in scheduleAlarm
            putExtra("alarm_id", alarmId)
            putExtra("name", alarm.label)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)

        val receiver = ComponentName(context, BootReceiver::class.java)

        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}
