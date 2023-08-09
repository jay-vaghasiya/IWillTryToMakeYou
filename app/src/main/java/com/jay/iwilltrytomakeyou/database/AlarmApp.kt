package com.jay.iwilltrytomakeyou.database

import android.app.Application
import androidx.room.Room

class AlarmApp:Application() {

    companion object{
        lateinit var alarmDatabase: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        alarmDatabase= Room.databaseBuilder(
            applicationContext,
            alarmDatabase::class.java,
            "alarm_database"
        ).build()
    }
}