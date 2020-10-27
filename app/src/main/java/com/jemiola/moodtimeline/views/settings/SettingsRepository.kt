package com.jemiola.moodtimeline.views.settings

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDOv2
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.FirstAndLastMoodDateText
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.model.localdatabase.LocalDatabase
import com.jemiola.moodtimeline.utils.rangepickers.RangeFormatter
import org.threeten.bp.LocalDate

class SettingsRepository : BaseRepository() {

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()

    private val rangeFormatter = RangeFormatter()

    fun getAllTimetableMoods(
        callback: OnRepositoryCallback<List<TimelineMoodBOv2>>
    ) {
        launchCallbackRequest(
            request = {
                database.timelineMoodDaoV2().getAllMoods()
            },
            onSuccess = {
                val timelineMoodBOs = convertTimelineMoodDOtoBO(it)
                val moodsInCorrectOrder = timelineMoodBOs.reversed()
                callback.onSuccess(moodsInCorrectOrder)
            },
            onError = { callback.onError() }
        )
    }

    fun getAllTimetableMoodsWithRange(
        callback: OnRepositoryCallback<List<TimelineMoodBOv2>>,
        from: LocalDate,
        to: LocalDate
    ) {
        launchCallbackRequest(
            request = {
                database.timelineMoodDaoV2().getMoodsFromTo(from, to)
            },
            onSuccess = {
                val timelineMoodBOs = convertTimelineMoodDOtoBO(it)
                val moodsInCorrectOrder = timelineMoodBOs.reversed()
                callback.onSuccess(moodsInCorrectOrder)
            },
            onError = { callback.onError() }
        )
    }

    fun getMoodsFirstAndLastDate(
        callback: OnRepositoryCallback<FirstAndLastMoodDateText>
    ) {
        launchCallbackRequest(
            request = {
                database.timelineMoodDaoV2().getAllMoods()
            },
            onSuccess = {
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