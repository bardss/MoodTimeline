package com.jemiola.moodtimeline.views.detailstimelinemood

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.model.localdatabase.LocalDatabase

class DetailsTimelineMoodRepository : BaseRepository() {

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalDatabase::class.java, "details-timetable-mood-database"
    ).build()

}