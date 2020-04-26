package com.jemiola.moodtimeline.views.timeline

import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO

interface TimelineContract {
    interface Presenter {
        fun requestTimelineMoods()
        fun getDefaultFromDate(): String
        fun getDefaultToDate(): String
        fun createDateTextFrom(dayOfMonth: Int, monthOfYear: Int, year: Int): String
        fun getSearchFromDateLong(): Long
        fun getSearchToDateLong(): Long
    }
    interface View {
        fun openEditTimelineMoodActivity(mood: TimelineMoodBO)
        fun openTimelineMoodDetails(mood: TimelineMoodBO)
        fun setTimelineMoods(moods: List<TimelineMoodBO>)
        fun getFromDate(): String
        fun getToDate(): String
    }
}