package com.jay.iwilltrytomakeyou.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AlarmRepository(private val alarmDao:AlarmDao){
    fun getAllAlarmsLiveData(): Flow<List<Alarm>> {

        return alarmDao.getAllAlarmsLiveData()
    }
    suspend fun insertAlarm(alarm: Alarm) {
        withContext(Dispatchers.IO){
            alarmDao.insert(alarm)
        }
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        withContext(Dispatchers.IO){
            alarmDao.delete(alarm)
        }
    }

}