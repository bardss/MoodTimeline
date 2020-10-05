package com.jemiola.moodtimeline.model.localdatabase

import androidx.room.TypeConverter
import com.jemiola.moodtimeline.model.data.databaseobjects.MoodDO
import org.threeten.bp.LocalDate

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): LocalDate {
        return LocalDate.ofEpochDay(value)
    }

    @TypeConverter
    fun toTimestamp(date: LocalDate): Long {
        return date.toEpochDay()
    }

    @TypeConverter
    fun toInt(value: String): MoodDO {
        return MoodDO.valueOf(value)
    }

    @TypeConverter
    fun fromInt(mood: MoodDO): String {
        return mood.toString()
    }

    @TypeConverter
    fun toToListOfStrings(paths: String): List<String> {
        return paths.split(",").map { it.trim() }
    }

    @TypeConverter
    fun fromListOfStrings(paths: List<String>): String {
        return paths.joinToString(",")
    }
}