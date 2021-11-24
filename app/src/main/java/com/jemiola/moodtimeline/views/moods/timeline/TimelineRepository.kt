package com.jemiola.moodtimeline.views.moods.timeline

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.localdatabase.LocalSQLDatabase
import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDOv2
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.model.data.local.convertTimelineMoodDOtoBO
import com.jemiola.moodtimeline.utils.DefaultTime
import org.threeten.bp.LocalDate

class TimelineRepository : BaseRepository() {

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalSQLDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()

    fun getTimetableMoodsPaged(
        nextPage: Int,
        pageSize: Int,
        callback: OnRepositoryCallback<List<TimelineMoodBOv2>>
    ) {
        launchCallbackRequest(
            request = {
                database.timelineMoodDaoV2().getMoodsPaged(pageSize, nextPage)
            },
            onSuccess = {
                val timelineMoodBOs = it
                    .convertTimelineMoodDOtoBO()
                    .sortedBy { mood -> mood.date }
                callback.onSuccess(timelineMoodBOs)
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
}