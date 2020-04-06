package com.jemiola.moodtimeline.views.timeline

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.utils.DefaultClock
import com.jemiola.moodtimeline.utils.pushToFront
import org.threeten.bp.LocalDate

class TimelinePresenter(
    private val view: TimelineContract.View,
    override val repository: TimelineRepository
) : BasePresenter(repository), TimelineContract.Presenter {

    override fun refreshTimelineMoods() {
        val callback = createRepositoryCallback<List<TimelineMoodBO>>(
            onSuccessAction = { onGetTimetableMoodsSuccess(it) },
            onErrorAction = {}
        )
        repository.getTimetableMoods(callback)
    }

    private fun onGetTimetableMoodsSuccess(result: List<TimelineMoodBO>) {
        val moods = addAddMoodIfNeeded(result)
        view.setTimelineMoods(moods)
    }

    private fun addAddMoodIfNeeded(moodsFromRepository: List<TimelineMoodBO>): List<TimelineMoodBO> {
        return if (shouldAddMoodBeVisible(moodsFromRepository)) {
            val addTimelineItem = createAddTimelineMood()
            moodsFromRepository.pushToFront(addTimelineItem)
        } else {
            val addTimelineItem = createAddTimelineMood()
            moodsFromRepository.pushToFront(addTimelineItem)
        }
    }

    private fun createAddTimelineMood(): TimelineMoodBO {
        return TimelineMoodBO(
            LocalDate.now(DefaultClock.getClock()),
            "",
            CircleMoodBO.NONE,
            CircleStateBO.ADD
        )
    }

    private fun shouldAddMoodBeVisible(moods: List<TimelineMoodBO>): Boolean {
        return moods.none { it.date == LocalDate.now(DefaultClock.getClock()) }
    }

}