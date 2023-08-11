package com.jay.iwilltrytomakeyou.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val dateTime:Long,
    val dayOfWeek: MutableList<Int>,
    val label:String,
    var isActive:Boolean)
