package com.jemiola.moodtimeline.views.edittimelinemood

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.model.localdatabase.LocalDatabase
import com.jemiola.moodtimeline.model.data.databaseobjects.MoodDO
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO

class EditTimelineMoodRepository : BaseRepository() {

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalDatabase::class.java, "edit-timetable-mood-database"
    ).build()

    fun addMood(timelineMood: TimelineMoodBO) {
        launchNoCallbackRequest {
            val timelineMoodDO = convertMoodBOtoDO(timelineMood)
            database.timelineMoodDao().insert(timelineMoodDO)
        }
    }

    private fun convertMoodBOtoDO(mood: TimelineMoodBO): TimelineMoodDO {
        return TimelineMoodDO(
            mood.note,
            mood.date,
            MoodDO.from(mood.mood)
        )
    }

}