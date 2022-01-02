package com.jemiola.moodtimeline.views.moods.search

import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import org.threeten.bp.LocalDate
import java.util.*

interface SearchContract {
    interface Presenter {
        fun getDefaultFromDate(locale: Locale): String
        fun getDefaultToDate(locale: Locale): String
        fun searchTimelineMoods(fromDate: LocalDate, toDate: LocalDate)
//        fun getSearchFromDateLong(): Long
//        fun getSearchToDateLong(): Long
        fun createDateTextFrom(
            locale: Locale,
            dayOfMonth: Int,
            monthOfYear: Int,
            year: Int
        ): String
    }
    interface View {
        fun getFromDate(): String
        fun getToDate(): String
        fun setMoods(moods: List<TimelineMoodBOv2>)
        fun showSearchEmptyView()
        fun showTimelineRecyclerView()
    }
}