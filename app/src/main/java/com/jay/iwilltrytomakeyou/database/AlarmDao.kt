package com.jay.iwilltrytomakeyou.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(alarm: Alarm)

    @Delete
    fun delete(alarm: Alarm)

    @Query("SELECT * FROM alarms")
    fun getAllAlarmsLiveData(): Flow<List<Alarm>>
}
