package com.jay.iwilltrytomakeyou.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow

class AlarmViewModel(application: Application):AndroidViewModel(application) {

    private val alarmRepository=AlarmRepository(AlarmApp.alarmDatabase.alarmDao())

    val allAlarmLiveData: Flow<List<Alarm>> = alarmRepository.getAllAlarmsLiveData()

    suspend fun insertAlarm(alarm: Alarm){
        alarmRepository.insertAlarm(alarm)
    }
    suspend fun updateAlarm(alarm: Alarm){
        alarmRepository.updateAlarm(alarm)
    }
    suspend fun deleteAlarm(alarm: Alarm){
        alarmRepository.deleteAlarm(alarm)
    }

    suspend fun isActiveAlarm(alarm: Alarm){
        alarmRepository.toggleAlarmActive(alarm)
    }
}