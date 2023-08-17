package com.jay.iwilltrytomakeyou.database

import androidx.room.TypeConverter

class TypeConverter {
    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString { ", " }// Convert ArrayList to comma-separated string
    }

    @TypeConverter
    fun toList(data: String): List<String> {
        return data.split(", ")
    }
}