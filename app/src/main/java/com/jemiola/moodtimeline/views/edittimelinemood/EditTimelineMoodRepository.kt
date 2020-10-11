package com.jemiola.moodtimeline.views.edittimelinemood

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.localdatabase.LocalDatabase
import com.jemiola.moodtimeline.model.data.databaseobjects.MoodDO
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDOv2
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2

class EditTimelineMoodRepository : BaseRepository() {

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()

    private var openedMoodId: Int? = null

    fun setOpenedMoodId(openedMoodId: Int?) {
        this.openedMoodId = openedMoodId
    }

    fun addMood(timelineMood: TimelineMoodBOv2, callback: OnRepositoryCallback<Unit>) {
        launchCallbackRequest(
            request = {
                val timelineMoodDO = convertMoodBOtoDO(timelineMood)
                database.timelineMoodDaoV2().insert(timelineMoodDO)
            },
            onSuccess = { callback.onSuccess(it) },
            onError = { callback.onError() }
        )
    }

    fun editMood(timelineMood: TimelineMoodBOv2, callback: OnRepositoryCallback<Unit>) {
        launchCallbackRequest(
            request = {
                val timelineMoodDO = convertMoodBOtoDO(timelineMood)
                database.timelineMoodDaoV2().update(timelineMoodDO)
            },
            onSuccess = { callback.onSuccess(it) },
            onError = { callback.onError() }
        )
    }

    private fun convertMoodBOtoDO(mood: TimelineMoodBOv2): TimelineMoodDOv2 {
        return TimelineMoodDOv2(
            id = openedMoodId,
            note = mood.note,
            date = mood.date,
            mood = MoodDO.from(mood.circleMood),
            picturesPaths = mood.picturesPaths
        )
    }

}