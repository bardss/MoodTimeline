package com.jemiola.moodtimeline.views.moods.timeline

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepositoryMVP
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.model.data.local.convertTimelineMoodsDOtoBO
import com.jemiola.moodtimeline.model.localdatabase.LocalSQLDatabase
import org.threeten.bp.LocalDate

class TimelineRepository : BaseRepositoryMVP() {

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalSQLDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()

    fun getMoodForDate(today: LocalDate, callback: OnRepositoryCallback<TimelineMoodBOv2>) {
        launchCallbackRequest(
            request = { database.timelineMoodDaoV2().getMoodForDate(today) },
            onSuccess = { callback.onSuccess(it.convertTimelineMoodsDOtoBO()) },
            onError = { callback.onError() }
        )
    }

    fun getTimetableMoodsPaged(
        pageIndex: Int,
        pageSize: Int,
        callback: OnRepositoryCallback<List<TimelineMoodBOv2>>
    ) {
        launchCallbackRequest(
            request = { database.timelineMoodDaoV2().getMoodsPaged(pageSize, pageIndex) },
            onSuccess = {
                val timelineMoodBOs = it
                    .convertTimelineMoodsDOtoBO()
                    .sortedByDescending { mood -> mood.date }
                callback.onSuccess(timelineMoodBOs)
            },
            onError = { callback.onError() }
        )
    }

    fun getMoodsCount(
        callback: OnRepositoryCallback<Int>
    ) {
        launchCallbackRequest(
            request = { database.timelineMoodDaoV2().getMoodsCount() },
            onSuccess = { callback.onSuccess(it) },
            onError = { callback.onError() }
        )
    }
}