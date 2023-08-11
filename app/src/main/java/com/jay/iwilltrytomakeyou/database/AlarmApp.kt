package com.jay.iwilltrytomakeyou.database

import android.app.Application
import androidx.room.Room

class AlarmApp:Application() {
    override fun onCreate() {
        super.onCreate()
        alarmDatabase= Room.databaseBuilder(
            applicationContext,
            alarmDatabase::class.java,
            "alarm_database"
        ).build()
    }
    companion object{
        lateinit var alarmDatabase: AppDatabase
            private set
    }
}