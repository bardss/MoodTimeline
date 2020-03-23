package com.jemiola.moodtimeline.model.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jemiola.moodtimeline.model.localdatabase.dao.TimetableMoodDao
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDO

@Database(entities = arrayOf(TimelineMoodDO::class), version = 1)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun timelineMoodDao(): TimetableMoodDao
}
