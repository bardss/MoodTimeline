package com.jemiola.moodtimeline.model.localdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDO

@Dao
interface TimetableMoodDao {
    @Query("SELECT * FROM timelinemooddo")
    fun getAll(): List<TimelineMoodDO>

    @Insert
    fun insert(timelineMood: TimelineMoodDO)

    @Update
    fun update(timelineMood: TimelineMoodDO)

}
