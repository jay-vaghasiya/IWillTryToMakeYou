package com.jay.iwilltrytomakeyou

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jay.iwilltrytomakeyou.database.Alarm
import com.jay.iwilltrytomakeyou.database.AlarmApp
import com.jay.iwilltrytomakeyou.database.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            GlobalScope.launch(Dispatchers.IO) {

                val alarms = retrieveAlarmFromDatabase()
                alarms.collect { alarmList ->
                    for (alarm in alarmList) {
                        scheduleAlarm(context, alarm)
                    }
                }
            }
        }
    }


    private fun retrieveAlarmFromDatabase(): Flow<List<Alarm>> {
        val alarmRepository = AlarmRepository(AlarmApp.alarmDatabase.alarmDao())

        return alarmRepository.getAllAlarmsLiveData()
    }


    private fun scheduleAlarm(context: Context?, alarm: Alarm) {
        val alarmManager = AlarmManager(context!!)
        val alarmDateTime = Calendar.getInstance()
        alarmDateTime.timeInMillis = alarm.dateTime


        alarmManager.scheduleAlarm(alarm.id, alarmDateTime,alarm)
    }

}