package com.jemiola.moodtimeline.views.settings.generatepdf

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDOv2
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.FirstAndLastMoodDateText
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.model.localdatabase.LocalSQLDatabase
import com.jemiola.moodtimeline.utils.rangepickers.RangeFormatter
import org.threeten.bp.LocalDate

class GeneratePdfRepository : BaseRepository() {

    private val databaseSQL = Room.databaseBuilder(
        BaseApplication.context,
        LocalSQLDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()

    suspend fun getAllTimetableMoodsWithRangeSuspend(
        from: LocalDate,
        to: LocalDate
    ): List<TimelineMoodBOv2> {
        val result = databaseSQL.timelineMoodDaoV2().getMoodsFromTo(from, to)
        val timelineMoodBOs = convertTimelineMoodDOtoBO(result)
        return timelineMoodBOs.reversed()
    }

    fun getMoodsFirstAndLastDate(
        callback: OnRepositoryCallback<FirstAndLastMoodDateText>
    ) {
        launchCallbackRequest(
            request = {
                databaseSQL.timelineMoodDaoV2().getAllMoods()
            },
            onSuccess = {
                val rangeFormatter = RangeFormatter()
                val firstMoodDate = it.first().date
                val lastMoodDate = it.last().date
                val firstMoodDateText =
                    firstMoodDate.format(rangeFormatter.getDefaultSearchDateFormatter())
                val lastMoodDateText =
                    lastMoodDate.format(rangeFormatter.getDefaultSearchDateFormatter())
                callback.onSuccess(FirstAndLastMoodDateText(firstMoodDateText, lastMoodDateText))
            },
            onError = { callback.onError() }
        )
    }

    private fun convertTimelineMoodDOtoBO(timetableMoodDOs: List<TimelineMoodDOv2>): List<TimelineMoodBOv2> {
        return timetableMoodDOs.map {
            TimelineMoodBOv2(
                id = it.id,
                date = it.date,
                note = it.note,
                circleMood = CircleMoodBO.from(it.mood),
                picturesPaths = it.picturesPaths
            )
        }
    }
}