package com.jemiola.moodtimeline.views.timeline

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.localdatabase.LocalDatabase
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDOv2
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.DefaultTime
import org.threeten.bp.LocalDate

class TimelineRepository : BaseRepository() {

    val defaultSearchFromDate: LocalDate = LocalDate.now(DefaultTime.getClock()).minusDays(14)
    val defaultSearchToDate: LocalDate = LocalDate.now(DefaultTime.getClock())

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()

    fun getTimetableMoods(
        from: LocalDate,
        to: LocalDate,
        callback: OnRepositoryCallback<List<TimelineMoodBOv2>>
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

    fun getTimetableMoodsCount(
        callback: OnRepositoryCallback<Int>
    ) {
        launchCallbackRequest(
            request = { database.timelineMoodDaoV2().getMoodsCount() },
            onSuccess = { callback.onSuccess(it) },
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
                picturePath = it.picturesPaths
            )
        }
    }
}