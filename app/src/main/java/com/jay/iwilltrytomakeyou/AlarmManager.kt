package com.jay.iwilltrytomakeyou

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

class AlarmManager(private val context: Context) {

    fun scheduleAlarm(alarmId: Long, alarmDataTime: Calendar) {

        val requestCode=alarmId.hashCode()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_ALARM_ID, alarmId)

        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alarmDataTime.timeInMillis,pendingIntent)
        }else{
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmDataTime.timeInMillis, pendingIntent)
        }
    }
    companion object{
        const val EXTRA_ALARM_ID="extra_alarm_id"
    }

}