package com.jemiola.moodtimeline.views.moods.timeline

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.DefaultTime
import com.jemiola.moodtimeline.utils.pushToFront
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class TimelinePresenter(
    private val view: TimelineContract.View,
    override val repository: TimelineRepository
) : BasePresenter(repository), TimelineContract.Presenter {

    init {
        requestTimetableMoodsPaged(0, 5)
    }

    override fun setupTimetableMoods() {
        val callback = createRepositoryCallback<Int>(
            onSuccessAction = { onGetTimetableMoodsCountSuccess(it) },
            onErrorAction = {}
        )
        repository.getTimetableMoodsCount(callback)
    }

    private fun onGetTimetableMoodsCountSuccess(moodsCount: Int) {
        if (moodsCount == 0) view.showAddEmptyView()
        else {
            view.showBottomMenu()
//            requestTimetableMoods()
        }
    }

    fun requestTimetableMoodsPaged(nextPage: Int, pageSize: Int) {
        val callback = createRepositoryCallback<List<TimelineMoodBOv2>>(
            onSuccessAction = { onGetTimetableMoodsSuccess(it) },
            onErrorAction = {}
        )
        repository.getTimetableMoodsPaged(nextPage, pageSize, callback)
    }

    private fun onGetTimetableMoodsSuccess(result: List<TimelineMoodBOv2>) {
        val moods = addSpecialMoodsIfNeeded(result)
//        val moods = result
        view.setPagedTimelineMoods(moods)
        if (moodsWithoutAddMoodState(moods)) {
            view.setupComeBackLaterView()
        }
    }

    private fun moodsWithoutAddMoodState(moods: List<TimelineMoodBOv2>): Boolean {
        return moods.none { it.circleState == CircleStateBO.ADD}
    }

    private fun addSpecialMoodsIfNeeded(moodsFromRepository: List<TimelineMoodBOv2>): List<TimelineMoodBOv2> {
        val editableMoods = moodsFromRepository.toMutableList()
        return when {
            true -> {
//            shouldAddMoodBeVisible(moodsFromRepository) -> {
                val addTimelineItem = createAddTimelineMood()
                editableMoods.pushToFront(addTimelineItem)
            }
            shouldEditMoodBeVisible(moodsFromRepository) -> {
                createMoodsWithFirstEditableMood(editableMoods)
            }
            else -> moodsFromRepository
        }
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

    private fun createMoodsWithFirstEditableMood(
        moodsFromRepository: MutableList<TimelineMoodBOv2>
    ): List<TimelineMoodBOv2> {
        moodsFromRepository[0].circleState = CircleStateBO.EDIT
        return moodsFromRepository
    }

    private fun shouldEditMoodBeVisible(moods: List<TimelineMoodBOv2>): Boolean {
        return moods.any { it.date == LocalDate.now(DefaultTime.getClock()) }
    }
}