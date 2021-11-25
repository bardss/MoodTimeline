package com.jemiola.moodtimeline.views.moods.search

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepository
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.model.data.local.convertTimelineMoodsDOtoBO
import com.jemiola.moodtimeline.model.localdatabase.LocalSQLDatabase
import com.jemiola.moodtimeline.utils.DefaultTime
import org.threeten.bp.LocalDate

class SearchRepository : BaseRepository() {

    val defaultSearchFromDate: LocalDate = LocalDate.now(DefaultTime.getClock()).minusDays(14)
    val defaultSearchToDate: LocalDate = LocalDate.now(DefaultTime.getClock())

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalSQLDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()

    fun getTimetableMoods(
        from: LocalDate,
        to: LocalDate,
        callback: OnRepositoryCallback<List<TimelineMoodBOv2>>
    ) {
        launchCallbackRequest(
            request = {
                database.timelineMoodDaoV2().getMoodsFromTo(from, to)
            },
            onSuccess = {
                val timelineMoodBOs = it
                    .convertTimelineMoodsDOtoBO()
                    .sortedByDescending { mood -> mood.date }
                callback.onSuccess(timelineMoodBOs)
            },
            onError = { callback.onError() }
        )
    }
}