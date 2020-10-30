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
import com.jemiola.moodtimeline.model.localdatabase.DatabaseKeys
import com.jemiola.moodtimeline.model.localdatabase.LocalNoSQLDatabase
import com.jemiola.moodtimeline.model.localdatabase.LocalSQLDatabase
import com.jemiola.moodtimeline.utils.rangepickers.RangeFormatter
import org.threeten.bp.LocalDate

class SettingsRepository : BaseRepository() {

    private val databaseSQL = Room.databaseBuilder(
        BaseApplication.context,
        LocalSQLDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()
    private val databaseNoSQL = LocalNoSQLDatabase.getInstance()

    private val rangeFormatter = RangeFormatter()

    fun saveAppTheme(themeKey: Int) {
        val editInstance = databaseNoSQL?.edit()
        editInstance?.putInt(DatabaseKeys.APP_THEME, themeKey)
        editInstance?.apply()
    }

    fun getAppTheme(): Int? {
        val errorValue = 500
        val theme = databaseNoSQL?.getInt(DatabaseKeys.APP_THEME, errorValue)
        return if (theme != 500) theme else null
    }

    fun getAllTimetableMoods(
        callback: OnRepositoryCallback<List<TimelineMoodBOv2>>
    ) {
        launchCallbackRequest(
            request = {
                databaseSQL.timelineMoodDaoV2().getAllMoods()
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
                databaseSQL.timelineMoodDaoV2().getMoodsFromTo(from, to)
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
                databaseSQL.timelineMoodDaoV2().getAllMoods()
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