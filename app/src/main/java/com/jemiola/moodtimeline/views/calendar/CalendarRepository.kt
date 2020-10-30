package com.jemiola.moodtimeline.views.calendar

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDOv2
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.model.localdatabase.LocalSQLDatabase
import com.jemiola.moodtimeline.utils.DefaultTime
import org.threeten.bp.LocalDate

class CalendarRepository : BaseRepository() {

    var currentMonthDate: LocalDate = LocalDate.now(DefaultTime.getClock())

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalSQLDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()

    fun getCurrentMonthMoods(
        callback: OnRepositoryCallback<List<TimelineMoodBOv2>>
    ) {
        val monthStartDay = currentMonthDate.withDayOfMonth(1)
        val monthEndDay = currentMonthDate.withDayOfMonth(currentMonthDate.lengthOfMonth())
        launchCallbackRequest(
            request = {
                database.timelineMoodDaoV2().getMoodsFromTo(monthStartDay, monthEndDay)
            },
            onSuccess = {
                val timelineMoodBOs = convertTimetableMoodDOtoBO(it)
                callback.onSuccess(timelineMoodBOs)
            },
            onError = { callback.onError() }
        )
    }

    private fun convertTimetableMoodDOtoBO(timetableMoodDOs: List<TimelineMoodDOv2>): List<TimelineMoodBOv2> {
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