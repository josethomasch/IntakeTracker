package com.example.intaketracker.data.local



import androidx.room.TypeConverter

import java.time.LocalTime



class Converters {

    @TypeConverter

    fun fromLocalTime(time: LocalTime?): String? = time?.toString()



    @TypeConverter

    fun toLocalTime(timeString: String?): LocalTime? = timeString?.let { LocalTime.parse(it) }



    @TypeConverter

    fun fromItemType(type: ItemType): String = type.name



    @TypeConverter

    fun toItemType(name: String): ItemType = ItemType.valueOf(name)

}

