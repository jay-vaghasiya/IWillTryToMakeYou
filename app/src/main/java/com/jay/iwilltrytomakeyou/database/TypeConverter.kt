package com.jay.iwilltrytomakeyou.database

import androidx.room.TypeConverter

class TypeConverter {
    @TypeConverter
    fun fromList(dayOfWeek:MutableList<Int>):String {
        return dayOfWeek.joinToString { "," }
    }

    @TypeConverter
    fun toList(dayOfWeekString: String):MutableList<Int>{
        return dayOfWeekString.split(",").map { it.toInt() }.toMutableList()
    }
}