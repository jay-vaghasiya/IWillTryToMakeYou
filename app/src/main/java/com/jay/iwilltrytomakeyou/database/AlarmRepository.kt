package com.jay.iwilltrytomakeyou.database

import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val alarmDao:AlarmDao){
    fun getAllAlarmsLiveData(): Flow<List<Alarm>> {

        return alarmDao.getAllAlarmsLiveData()
    }
    suspend fun insertAlarm(alarm: Alarm) {
        alarmDao.insert(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.delete(alarm)
    }

}