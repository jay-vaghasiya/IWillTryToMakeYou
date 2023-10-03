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
            putExtra(EXTRA_ALARM_ID, alarmId)
            putExtra("name", alarm.label)
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                alarmManager.canScheduleExactAlarms(
                ).apply {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        alarmDataTime.timeInMillis,
                        PendingIntent.getBroadcast(context,alarm.id,alarmIntent,PendingIntent.FLAG_IMMUTABLE
                                or PendingIntent.FLAG_UPDATE_CURRENT)
                    )
                } else -> {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    alarmDataTime.timeInMillis,
                    PendingIntent.getBroadcast(context,alarm.id,alarmIntent,PendingIntent.FLAG_IMMUTABLE
                            or PendingIntent.FLAG_UPDATE_CURRENT)
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
            action = "ALARM_TRIGGERED"
            putExtra("alarm_id", alarmId)
            putExtra("name", alarm.label)
        }

        alarmManager.cancel(PendingIntent.getBroadcast(context,alarm.id,intent,PendingIntent.FLAG_IMMUTABLE
                or PendingIntent.FLAG_UPDATE_CURRENT))

        val receiver = ComponentName(context, BootReceiver::class.java)

        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    companion object {
        const val EXTRA_ALARM_ID = "extra_alarm_id"
    }
}
