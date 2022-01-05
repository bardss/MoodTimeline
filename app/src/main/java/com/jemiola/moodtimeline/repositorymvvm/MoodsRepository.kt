package com.jemiola.moodtimeline.repositorymvvm

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.model.data.local.convertTimelineMoodsDOtoBO
import com.jemiola.moodtimeline.model.localdatabase.LocalSQLDatabase
import org.threeten.bp.LocalDate

class MoodsRepository {

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalSQLDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()

    fun getMoodsFor(
        from: LocalDate,
        to: LocalDate
    ): List<TimelineMoodBOv2> {
        return database.timelineMoodDaoV2().getMoodsFromTo(from, to).convertTimelineMoodsDOtoBO()
    }
}