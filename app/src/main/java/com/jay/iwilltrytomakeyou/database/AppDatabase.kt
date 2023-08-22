package com.jay.iwilltrytomakeyou.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun alarmDao():AlarmDao
}