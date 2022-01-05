package com.jemiola.moodtimeline.views.moods.search

import com.jemiola.moodtimeline.base.BasePresenterMVP
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.DateFormatterUtil
import com.jemiola.moodtimeline.utils.DefaultTime
import org.threeten.bp.LocalDate
import java.util.*

class SearchPresenter(
    private val view: SearchContract.View,
    override val repository: SearchRepository
) : BasePresenterMVP(repository), SearchContract.Presenter {

    private val dateFormatter = DateFormatterUtil()

    override fun searchTimelineMoods(fromDate: LocalDate, toDate: LocalDate) {
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

    override fun getDefaultFromDate(locale: Locale): String {
        val defaultFromDate = repository.defaultSearchFromDate
        return dateFormatter.getFormattedDate(locale, defaultFromDate)
    }

    override fun getDefaultToDate(locale: Locale): String {
        val defaultToDate = repository.defaultSearchToDate
        return dateFormatter.getFormattedDate(locale, defaultToDate)
    }

    override fun createDateTextFrom(
        locale: Locale,
        dayOfMonth: Int,
        monthOfYear: Int,
        year: Int
    ): String {
        val selectedDate = LocalDate.of(year, monthOfYear, dayOfMonth)
        return dateFormatter.getFormattedDate(locale, selectedDate)
    }

//    override fun getSearchFromDateLong(): Long {
//        val fromDate = getFromDateFromView()
//        return getMilisFromDate(fromDate)
//    }
//
//    override fun getSearchToDateLong(): Long {
//        val toDate = getToDateFromView()
//        return getMilisFromDate(toDate)
//    }

//    private fun getMilisFromDate(date: LocalDate): Long {
//        return LocalDateTime
//            .of(date, LocalTime.NOON)
//            .atZone(DefaultTime.getZone())
//            .toInstant()
//            .toEpochMilli()
//    }
//
//    private fun getFromDateFromView(locale: Locale): LocalDate {
//        val fromDateText = view.getFromDate()
//        return dateFormatter.getDateFromFormattedString(locale, fromDateText)
//    }
//
//    private fun getToDateFromView(locale: Locale): LocalDate {
//        val toDateText = view.getToDate()
//        return dateFormatter.getDateFromFormattedString(locale, toDateText)
//    }

    private fun changeTodayMoodToEditableStateIfNeeded(moods: List<TimelineMoodBOv2>) {
        val moodAddedToday =
            moods.firstOrNull { it.date == LocalDate.now(DefaultTime.getClock()) }
        if (moodAddedToday != null) {
            moodAddedToday.circleState = CircleStateBO.EDIT
        }
    }
}