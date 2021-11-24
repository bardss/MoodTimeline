package com.jemiola.moodtimeline.views.moods.search

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.DefaultTime
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class SearchPresenter(
    private val view: SearchContract.View,
    override val repository: SearchRepository
) : BasePresenter(repository), SearchContract.Presenter {

    override fun searchTimelineMoods() {
        val fromDate = getFromDateFromView()
        val toDate = getToDateFromView()
        val callback = createRepositoryCallback<List<TimelineMoodBOv2>>(
            onSuccessAction = { onSearchTimelineMoodsSuccess(it) },
            onErrorAction = {}
        )
        repository.getTimetableMoods(fromDate, toDate, callback)
    }

    private fun onSearchTimelineMoodsSuccess(moods: List<TimelineMoodBOv2>) {
        if (moods.isEmpty()) view.showSearchEmptyView()
        else {
            changeTodayMoodToEditableStateIfNeeded(moods)
            view.showTimelineRecyclerView()
            view.setMoods(moods)
        }
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

    private fun getDefaultSearchDateFormatter() =
        DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(Locale.ENGLISH)

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

    private fun getMilisFromDate(date: LocalDate): Long {
        return LocalDateTime
            .of(date, LocalTime.NOON)
            .atZone(DefaultTime.getZone())
            .toInstant()
            .toEpochMilli()
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

    private fun changeTodayMoodToEditableStateIfNeeded(moods: List<TimelineMoodBOv2>) {
        val moodAddedToday =
            moods.firstOrNull { it.date == LocalDate.now(DefaultTime.getClock()) }
        if (moodAddedToday != null) {
            moodAddedToday.circleState = CircleStateBO.EDIT
        }
    }
}