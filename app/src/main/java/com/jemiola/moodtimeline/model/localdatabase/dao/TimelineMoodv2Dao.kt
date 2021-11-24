package com.jemiola.moodtimeline.model.localdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDO
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDOv2
import org.threeten.bp.LocalDate

@Dao
interface TimelineMoodv2Dao {
    @Query("SELECT * FROM timelinemooddov2")
    fun getAllMoods(): List<TimelineMoodDOv2>

    @Query("SELECT * FROM timelinemooddov2 WHERE date BETWEEN :fromDate AND :toDate")
    fun getMoodsFromTo(fromDate: LocalDate, toDate: LocalDate): List<TimelineMoodDOv2>

    @Query("SELECT * FROM timelinemooddov2 ORDER BY date DESC LIMIT :pageSize OFFSET :pageIndex")
    fun getMoodsPaged(pageSize: Int, pageIndex: Int): List<TimelineMoodDOv2>

    @Query("SELECT COUNT(*) FROM timelinemooddov2")
    fun getMoodsCount(): Int

    @Insert
    fun insert(timelineMood: TimelineMoodDOv2)

    @Update
    fun update(timelineMood: TimelineMoodDOv2)
}
