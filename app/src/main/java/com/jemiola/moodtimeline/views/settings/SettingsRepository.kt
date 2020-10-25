package com.jemiola.moodtimeline.views.settings

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDOv2
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.model.localdatabase.LocalDatabase

class SettingsRepository : BaseRepository() {

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()

    fun getTimetableMoods(
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