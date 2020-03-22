package com.jemiola.moodtimeline.timeline.repository

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.data.databaseobjects.TimelineMoodDO
import com.jemiola.moodtimeline.data.local.CircleMoodBO
import com.jemiola.moodtimeline.data.local.TimelineMoodBO
import kotlinx.coroutines.async

class TimelineRepository : BaseRepository() {

    private val timetableDatabase = Room.databaseBuilder(
        BaseApplication.context,
        TimelineDatabase::class.java, "timetable-database"
    ).build()

    suspend fun getTimetableMoods(): List<TimelineMoodBO> {
        val timetableMoodDOsDeferred = async {
            timetableDatabase.timetableMoodDao().getAll()
        }
        val timetableMoodDOs = timetableMoodDOsDeferred.await()
        return convertTimetableMoodDOtoBO(timetableMoodDOs)
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