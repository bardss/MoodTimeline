package com.jemiola.moodtimeline.views.moods.timeline

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.DefaultTime
import org.threeten.bp.LocalDate

class TimelinePresenter(
    private val view: TimelineContract.View,
    override val repository: TimelineRepository
) : BasePresenter(repository), TimelineContract.Presenter {

    override fun updateTodaysMood() {
        val today = LocalDate.now(DefaultTime.getClock())
        val callback = createRepositoryCallback<TimelineMoodBOv2>(
            onSuccessAction = {
                it.circleState = CircleStateBO.EDIT
                view.updateTodaysMood(it)
                view.showTimelineRecyclerView()
            },
            onErrorAction = {}
        )
        repository.getMoodForDate(today, callback)
    }

    override fun setupTimetableMoods() {
        val callback = createRepositoryCallback<Int>(
            onSuccessAction = { onGetTimetableMoodsCountSuccess(it) },
            onErrorAction = {}
        )
        repository.getMoodsCount(callback)
    }

    private fun onGetTimetableMoodsCountSuccess(moodsCount: Int) {
        when (moodsCount) {
            0 -> view.showAddEmptyView()
            else -> {
                view.showBottomMenu()
                requestTimetableMoodsPaged(0, 5)
            }
        }
    }

    fun requestTimetableMoodsPaged(pageIndex: Int, pageSize: Int) {
        val callback = createRepositoryCallback<List<TimelineMoodBOv2>>(
            onSuccessAction = { onGetTimetableMoodsSuccess(it, pageIndex) },
            onErrorAction = {}
        )
        repository.getTimetableMoodsPaged(pageIndex, pageSize, callback)
    }

    private fun onGetTimetableMoodsSuccess(result: List<TimelineMoodBOv2>, pageIndex: Int) {
        val moods = if (pageIndex == 0) getMoodsWithAddEditMood(result) else result
        if (pageIndex == 0) view.setTimelineMoods(moods)
        else view.setPagedTimelineMoods(moods)
        if (moodsWithoutAddMoodState(moods)) {
            view.setupComeBackLaterView()
        }
    }

    private fun moodsWithoutAddMoodState(moods: List<TimelineMoodBOv2>): Boolean {
        return moods.none { it.circleState == CircleStateBO.ADD }
    }

    override fun createAddTimelineMood(): TimelineMoodBOv2 {
        return TimelineMoodBOv2(
            id = null,
            date = LocalDate.now(DefaultTime.getClock()),
            note = "",
            circleMood = CircleMoodBO.NONE,
            circleState = CircleStateBO.ADD,
            picturesPaths = listOf()
        )
    }

    private fun getMoodsWithAddEditMood(moods: List<TimelineMoodBOv2>): List<TimelineMoodBOv2> {
        val moodAddedToday =
            moods.firstOrNull { it.date == LocalDate.now(DefaultTime.getClock()) }
        return if (moodAddedToday != null) {
            moodAddedToday.circleState = CircleStateBO.EDIT
            moods
        } else {
            val addTimelineItem = createAddTimelineMood()
            listOf(addTimelineItem) + moods
        }
    }
}