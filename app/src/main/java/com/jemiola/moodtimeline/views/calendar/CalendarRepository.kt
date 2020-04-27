package com.jemiola.moodtimeline.views.calendar

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDO
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.model.localdatabase.LocalDatabase
import com.jemiola.moodtimeline.utils.DefaultTime
import org.threeten.bp.LocalDate

class CalendarRepository : BaseRepository() {

    var currentMonthDate: LocalDate = LocalDate.now(DefaultTime.getClock())

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()

    fun getCurrentMonthMoods(
        callback: OnRepositoryCallback<List<TimelineMoodBO>>
    ) {
        val monthStartDay = currentMonthDate.withDayOfMonth(1)
        val monthEndDay = currentMonthDate.withDayOfMonth(currentMonthDate.lengthOfMonth())
        launchCallbackRequest(
            request = {
                database.timelineMoodDao().getMoodsFromTo(monthStartDay, monthEndDay)
            },
            onSuccess = {
                val timelineMoodBOs = convertTimetableMoodDOtoBO(it)
                callback.onSuccess(timelineMoodBOs)
            },
            onError = { callback.onError() }
        )
    }

    private fun convertTimetableMoodDOtoBO(timetableMoodDOs: List<TimelineMoodDO>): List<TimelineMoodBO> {
        return timetableMoodDOs.map {
            TimelineMoodBO(
                id = it.id,
                date = it.date,
                note = it.note,
                circleMood = CircleMoodBO.from(it.mood),
                picturePath = it.picturePath
            )
        }
    }
}