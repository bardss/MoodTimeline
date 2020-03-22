package com.jemiola.moodtimeline.base

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jemiola.moodtimeline.dao.Converters
import com.jemiola.moodtimeline.dao.TimetableMoodDao
import com.jemiola.moodtimeline.data.databaseobjects.TimelineMoodDO

@Database(entities = arrayOf(TimelineMoodDO::class), version = 1)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun timetableMoodDao(): TimetableMoodDao
}
