package com.jemiola.moodtimeline.addtimelinemood.repository

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.base.LocalDatabase
import com.jemiola.moodtimeline.data.databaseobjects.MoodDO
import com.jemiola.moodtimeline.data.databaseobjects.TimelineMoodDO
import com.jemiola.moodtimeline.data.local.TimelineMoodBO
import kotlinx.coroutines.launch

class EditTimelineMoodRepository : BaseRepository() {

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalDatabase::class.java, "edit-timetable-mood-database"
    ).build()

    fun addMood(timelineMood: TimelineMoodBO) {
        launch {
            val timelineMoodDO = convertMoodBOtoDO(timelineMood)
            database.timetableMoodDao().insert(timelineMoodDO)
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