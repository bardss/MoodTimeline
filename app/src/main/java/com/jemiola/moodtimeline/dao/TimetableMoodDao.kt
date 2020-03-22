package com.jemiola.moodtimeline.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jemiola.moodtimeline.data.databaseobjects.TimelineMoodDO

@Dao
interface TimetableMoodDao {
    @Query("SELECT * FROM timelinemooddo")
    fun getAll(): List<TimelineMoodDO>

    @Insert
    fun insert(timelineMood: TimelineMoodDO)
}
