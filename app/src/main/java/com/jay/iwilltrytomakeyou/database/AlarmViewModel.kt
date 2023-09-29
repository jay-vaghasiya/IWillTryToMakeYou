package com.jay.iwilltrytomakeyou.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application):AndroidViewModel(application) {


    val allAlarmLiveData: Flow<List<Alarm>> = AlarmApp.alarmRepository.getAllAlarmsLiveData()


    suspend fun insertAlarm(alarm: Alarm) {
        viewModelScope.launch {
            AlarmApp.alarmRepository.insertAlarm(alarm)
        }

    }

    suspend fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            AlarmApp.alarmRepository.deleteAlarm(alarm)

        }
    }
}

