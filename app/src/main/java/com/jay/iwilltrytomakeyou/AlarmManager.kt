package com.jay.iwilltrytomakeyou

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

class AlarmManager(private val context: Context) {
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleAlarm(alarmId: Long, alarmDataTime: Calendar) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("alarm_id", alarmId)

        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.R){
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alarmDataTime.timeInMillis,pendingIntent)
        }else{
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmDataTime.timeInMillis, pendingIntent)
        }
    }

    fun cancelAlarm(alarmId: Long){
        val alarmManager=context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent=Intent(context,AlarmReceiver::class.java)
        val pendingIntent=PendingIntent.getBroadcast(context,alarmId.toInt(),intent,PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.cancel(pendingIntent)
    }

}