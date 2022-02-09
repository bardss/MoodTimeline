package com.jemiola.moodtimeline.repositorymvvm

import android.util.Log
import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.model.data.local.convertTimelineMoodsDOtoBO
import com.jemiola.moodtimeline.model.localdatabase.LocalSQLDatabase
import com.jemiola.moodtimeline.utils.DateFormatterUtil
import org.threeten.bp.LocalDate
import java.util.*

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

    fun getFirstMoodDate(): LocalDate = database.timelineMoodDaoV2().getFirstMoodDate()
    fun getLastMoodDate(): LocalDate = database.timelineMoodDaoV2().getLastMoodDate()
}