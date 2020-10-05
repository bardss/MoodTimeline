package com.jemiola.moodtimeline.model.localdatabase.dao

import androidx.room.*
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDO
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDOv2
import org.threeten.bp.LocalDate

@Dao
interface TimetableMoodDao {
    @Query("SELECT * FROM timelinemooddo")
    fun getAllMoods(): List<TimelineMoodDO>

    @Query("SELECT * FROM timelinemooddo WHERE date BETWEEN :fromDate AND :toDate")
    fun getMoodsFromTo(fromDate: LocalDate, toDate: LocalDate): List<TimelineMoodDO>

    @Query("SELECT COUNT(*) FROM timelinemooddo")
    fun getMoodsCount(): Int

    @Insert
    fun insert(timelineMood: TimelineMoodDO)

    @Update
    fun update(timelineMood: TimelineMoodDO)

    @Query("DELETE FROM timelinemooddo")
    fun deleteAll()
}
