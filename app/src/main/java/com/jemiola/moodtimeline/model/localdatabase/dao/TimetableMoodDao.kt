package com.jemiola.moodtimeline.model.localdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDO
import org.threeten.bp.LocalDate

@Dao
interface TimetableMoodDao {
    @Query("SELECT * FROM timelinemooddo WHERE date BETWEEN :fromDate AND :toDate")
    fun getMoodsFromTo(fromDate: LocalDate, toDate: LocalDate): List<TimelineMoodDO>

    @Query("SELECT COUNT(*) FROM timelinemooddo")
    fun getMoodsCount(): Int

    @Insert
    fun insert(timelineMood: TimelineMoodDO)

    @Update
    fun update(timelineMood: TimelineMoodDO)

}
