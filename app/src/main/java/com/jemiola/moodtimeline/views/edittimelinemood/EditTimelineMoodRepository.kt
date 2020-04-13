package com.jemiola.moodtimeline.views.edittimelinemood

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.localdatabase.LocalDatabase
import com.jemiola.moodtimeline.model.data.databaseobjects.MoodDO
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO

class EditTimelineMoodRepository : BaseRepository() {

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalDatabase::class.java, "edit-timetable-mood-database"
    ).build()

    private var openedMoodId: Int? = null

    fun setOpenedMoodId(openedMoodId: Int?) {
        this.openedMoodId = openedMoodId
    }

    fun addMood(timelineMood: TimelineMoodBO, callback: OnRepositoryCallback<Unit>) {
        launchCallbackRequest(
            request = {
                val timelineMoodDO = convertMoodBOtoDO(timelineMood)
                database.timelineMoodDao().insert(timelineMoodDO)
            },
            onSuccess = { callback.onSuccess(it) },
            onError = { callback.onError() }
        )
    }

    fun editMood(timelineMood: TimelineMoodBO, callback: OnRepositoryCallback<Unit>) {
        launchCallbackRequest(
            request = {
                val timelineMoodDO = convertMoodBOtoDO(timelineMood)
                database.timelineMoodDao().update(timelineMoodDO)
            },
            onSuccess = { callback.onSuccess(it) },
            onError = { callback.onError() }
        )
    }

    private fun convertMoodBOtoDO(mood: TimelineMoodBO): TimelineMoodDO {
        return TimelineMoodDO(
            id = openedMoodId,
            note = mood.note,
            date = mood.date,
            mood = MoodDO.from(mood.circleMood),
            picturePath = mood.picturePath
        )
    }

}