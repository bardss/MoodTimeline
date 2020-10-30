package com.jemiola.moodtimeline.model.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDO
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDOv2
import com.jemiola.moodtimeline.model.localdatabase.dao.TimelineMoodv2Dao
import com.jemiola.moodtimeline.model.localdatabase.dao.TimetableMoodDao

@Database(entities = arrayOf(
    TimelineMoodDO::class,
    TimelineMoodDOv2::class
), version = 2)
@TypeConverters(Converters::class)
abstract class LocalSQLDatabase : RoomDatabase() {
    abstract fun timelineMoodDao(): TimetableMoodDao
    abstract fun timelineMoodDaoV2(): TimelineMoodv2Dao
}
