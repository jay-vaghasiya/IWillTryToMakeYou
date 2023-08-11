package com.jay.iwilltrytomakeyou.database

import com.jay.iwilltrytomakeyou.database.AlarmApp.Companion.alarmDatabase
import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val alarmDao:AlarmDao){
    fun getAllAlarmsLiveData(): Flow<List<Alarm>> {

        return alarmDao.getAllAlarmsLiveData()
    }
    suspend fun insertAlarm(alarm: Alarm) {
        alarmDatabase.alarmDao().insert(alarm)
    }
    suspend fun updateAlarm(alarm: Alarm) {
        alarmDatabase.alarmDao().update(alarm)
    }
    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDatabase.alarmDao().delete(alarm)
    }
    suspend fun toggleAlarmActive(alarm: Alarm){
        val updateAlarm=alarm.copy(isActive = !alarm.isActive)
        alarmDatabase.alarmDao().update(updateAlarm)
    }
}