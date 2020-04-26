package com.jemiola.moodtimeline.views.timeline

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.utils.DefaultTime
import com.jemiola.moodtimeline.utils.pushToFront
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

class TimelinePresenter(
    private val view: TimelineContract.View,
    override val repository: TimelineRepository
) : BasePresenter(repository), TimelineContract.Presenter {

    override fun requestTimelineMoods() {
        val fromDate = getFromDateFromView()
        val toDate = getToDateFromView()
        val callback = createRepositoryCallback<List<TimelineMoodBO>>(
            onSuccessAction = { onGetTimetableMoodsSuccess(it) },
            onErrorAction = {}
        )
        repository.getTimetableMoods(fromDate, toDate, callback)
    }


    private fun getFromDateFromView(): LocalDate {
        val fromDateText = view.getFromDate()
        val formatter = getDefaultSearchDateFormatter()
        return LocalDate.parse(fromDateText, formatter)
    }

    private fun getToDateFromView(): LocalDate {
        val fromDateText = view.getToDate()
        val formatter = getDefaultSearchDateFormatter()
        return LocalDate.parse(fromDateText, formatter)
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
            date = LocalDate.now(DefaultTime.getClock()),
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
        val searchFromDate = getFromDateFromView()
        val searchToDate = getToDateFromView()
        val dateNow = LocalDate.now(DefaultTime.getClock())
        return moods.none { it.date == dateNow } &&
                dateNow.isAfter(searchFromDate) &&
                dateNow.isBefore(searchToDate)
    }

    private fun shouldEditMoodBeVisible(moods: List<TimelineMoodBO>): Boolean {
        return moods.any { it.date == LocalDate.now(DefaultTime.getClock()) }
    }

    override fun getDefaultFromDate(): String {
        val defaultFromDate = repository.defaultSearchFromDate
        val formatter = getDefaultSearchDateFormatter()
        return defaultFromDate.format(formatter)
    }

    override fun getDefaultToDate(): String {
        val defaultToDate = repository.defaultSearchToDate
        val formatter = getDefaultSearchDateFormatter()
        return defaultToDate.format(formatter)
    }

    private fun getDefaultSearchDateFormatter() = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun createDateTextFrom(dayOfMonth: Int, monthOfYear: Int, year: Int): String {
        val selectedDate = LocalDate.of(year, monthOfYear, dayOfMonth)
        val formatter = getDefaultSearchDateFormatter()
        return selectedDate.format(formatter)
    }

    override fun getSearchFromDateLong(): Long {
        val fromDate = getFromDateFromView()
        return getMilisFromDate(fromDate)
    }

    override fun getSearchToDateLong(): Long {
        val toDate = getToDateFromView()
        return getMilisFromDate(toDate)
    }

    private fun getMilisFromDate(date: LocalDate): Long{
        return LocalDateTime
            .of(date, LocalTime.NOON)
            .atZone(DefaultTime.getZone())
            .toInstant()
            .toEpochMilli()
    }

}