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
        val editableMoods = moodsFromRepository.toMutableList()
        return when {
            shouldAddMoodBeVisible(moodsFromRepository) -> {
                val addTimelineItem = createAddTimelineMood()
                editableMoods.pushToFront(addTimelineItem)
            }
            shouldEditMoodBeVisible(moodsFromRepository) -> {
                createMoodsWithFirstEditableMood(editableMoods)
            }
            else -> moodsFromRepository
        }
    }

    private fun createAddTimelineMood(): TimelineMoodBO {
        return TimelineMoodBO(
            id = null,
            date = LocalDate.now(DefaultClock.getClock()),
            note = "",
            circleMood = CircleMoodBO.NONE,
            circleState = CircleStateBO.ADD,
            picturePath = ""
        )
    }

    private fun createMoodsWithFirstEditableMood(
        moodsFromRepository: MutableList<TimelineMoodBO>
    ): List<TimelineMoodBO> {
        moodsFromRepository[0].circleState = CircleStateBO.EDIT
        return moodsFromRepository
    }

    private fun shouldAddMoodBeVisible(moods: List<TimelineMoodBO>): Boolean {
        return moods.none { it.date == LocalDate.now(DefaultClock.getClock()) }
    }

    private fun shouldEditMoodBeVisible(moods: List<TimelineMoodBO>): Boolean {
        return moods.any { it.date == LocalDate.now(DefaultClock.getClock()) }
    }

}