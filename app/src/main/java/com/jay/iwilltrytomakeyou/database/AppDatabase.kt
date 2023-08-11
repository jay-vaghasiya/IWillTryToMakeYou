package com.jay.iwilltrytomakeyou.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Alarm::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase:RoomDatabase() {
    abstract fun alarmDao():AlarmDao
}