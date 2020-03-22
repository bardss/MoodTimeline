package com.jemiola.moodtimeline.timeline

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.data.local.CircleMoodBO
import com.jemiola.moodtimeline.data.local.CircleStateBO
import com.jemiola.moodtimeline.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.timeline.repository.TimelineRepository
import com.jemiola.moodtimeline.utils.DefaultClock
import com.jemiola.moodtimeline.utils.pushToFront
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class TimelinePresenter(
    private val view: TimelineContract.View,
    private val repository: TimelineRepository
) : BasePresenter, TimelineContract.Presenter {

    override val job = Job()

    override fun setupTimelineMoods() {
        launch(Dispatchers.Main) {
            val moodsFromRepository = repository.getTimetableMoods()
            val moods = addAddMoodIfNeeded(moodsFromRepository)
            view.setTimelineMoods(moods)
        }
    }

    private fun addAddMoodIfNeeded(moodsFromRepository: List<TimelineMoodBO>): List<TimelineMoodBO> {
        return if (shouldAddMoodBeVisible(moodsFromRepository)) {
            val addTimelineItem = createAddTimelineMood()
            moodsFromRepository.pushToFront(addTimelineItem)
        } else moodsFromRepository
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