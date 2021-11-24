package com.jemiola.moodtimeline.views.moods.search

import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2

interface SearchContract {
    interface Presenter {
        fun getDefaultFromDate(): String
        fun getDefaultToDate(): String
        fun searchTimelineMoods()
        fun getSearchFromDateLong(): Long
        fun getSearchToDateLong(): Long
        fun createDateTextFrom(dayOfMonth: Int, monthOfYear: Int, year: Int): String
    }
    interface View {
        fun getFromDate(): String
        fun getToDate(): String
        fun setMoods(moods: List<TimelineMoodBOv2>)
        fun showSearchEmptyView()
        fun showTimelineRecyclerView()
    }
}