package com.jemiola.moodtimeline.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jemiola.moodtimeline.data.databaseobjects.TimelineMoodDO

@Dao
interface TimetableMoodDao {
    @Query("SELECT * FROM timelinemooddo")
    suspend fun getAll(): List<TimelineMoodDO>

    @Insert
    suspend fun insert(user: TimelineMoodDO)
}
