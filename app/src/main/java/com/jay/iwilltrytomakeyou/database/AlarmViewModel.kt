package com.jay.iwilltrytomakeyou.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application):AndroidViewModel(application) {

    private val alarmRepository=AlarmRepository(AlarmApp.alarmDatabase.alarmDao())

    val allAlarmLiveData: Flow<List<Alarm>> = alarmRepository.getAllAlarmsLiveData()

    fun insertAlarm(alarm: Alarm){
        viewModelScope.launch {
            alarmRepository.insertAlarm(alarm)
        }

    }
    fun updateAlarm(alarm: Alarm){
        viewModelScope.launch {
        alarmRepository.updateAlarm(alarm)
        }
    }
    fun deleteAlarm(alarm: Alarm){
        viewModelScope.launch {
        alarmRepository.deleteAlarm(alarm)

        }
    }

    fun isActiveAlarm(alarm: Alarm){
        viewModelScope.launch {
        alarmRepository.toggleAlarmActive(alarm)
        }
    }
}