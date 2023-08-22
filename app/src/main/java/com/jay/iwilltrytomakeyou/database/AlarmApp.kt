package com.jay.iwilltrytomakeyou.database

import android.app.Application
import androidx.room.Room

class AlarmApp:Application() {
    override fun onCreate() {
        super.onCreate()
        alarmDatabase= Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "alarm_database"
        ).build()
        alarmRepository= AlarmRepository(alarmDatabase.alarmDao())
    }
    companion object{
        lateinit var alarmDatabase: AppDatabase
        private set

        lateinit var alarmRepository: AlarmRepository
        private set
    }
}