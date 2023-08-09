package com.jay.iwilltrytomakeyou.database

import kotlinx.coroutines.flow.Flow

class AlarmRepository (private val alarmDao: AlarmDao){
    fun getAllAlarmsFlow(): Flow<List<Alarm>>{

        return alarmDao.getAllAlarms()
    }
}