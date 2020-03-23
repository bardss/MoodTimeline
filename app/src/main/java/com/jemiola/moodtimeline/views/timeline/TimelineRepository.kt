package com.jemiola.moodtimeline.views.timeline

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.localdatabase.LocalDatabase
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDO
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO

class TimelineRepository : BaseRepository() {

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalDatabase::class.java, "edit-timetable-mood-database"
    ).build()

    fun getTimetableMoods(callback: OnRepositoryCallback<List<TimelineMoodBO>>) {
        launchCallbackRequest(
            request = {
                database.timelineMoodDao().getAll()
            },
            callback = {
                val timelineMoodBOs = convertTimetableMoodDOtoBO(it)
                callback.onSuccess(timelineMoodBOs)
            }
        )
    }

    private fun convertTimetableMoodDOtoBO(timetableMoodDOs: List<TimelineMoodDO>): List<TimelineMoodBO> {
        return timetableMoodDOs.map {
            TimelineMoodBO(
                it.date,
                it.note,
                CircleMoodBO.from(it.mood)
            )
        }
    }
}