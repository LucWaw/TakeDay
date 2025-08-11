package com.lucwaw.takeday.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lucwaw.takeday.domain.model.TriState
import java.time.LocalDate
import java.time.LocalTime

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromMap(value: Map<String, TriState>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toMap(value: String): Map<String, TriState> {
        val type = object : TypeToken<Map<String, TriState>>() {}.type
        return gson.fromJson(value, type) ?: emptyMap()
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate = LocalDate.parse(dateString)

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? = time?.toString()

    @TypeConverter
    fun toLocalTime(timeString: String?): LocalTime? =
        timeString?.let { LocalTime.parse(it) }
}
