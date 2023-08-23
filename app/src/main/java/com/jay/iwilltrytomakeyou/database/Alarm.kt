package com.jay.iwilltrytomakeyou.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")

data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val dateTime: Long,
    val label:String,
    var isActive:Boolean
    )
