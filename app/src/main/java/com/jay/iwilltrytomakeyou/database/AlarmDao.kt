package com.jay.iwilltrytomakeyou.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlarmDao {

    @Insert
    fun insert(alarm: Alarm):Long

    @Update
    fun update(alarm: Alarm)

    @Delete
    fun delete(alarm: Alarm)

    @Query("SELECT * FROM alarms")
    fun getAllAlarms():List<Alarm>

}