package com.jemiola.moodtimeline.views.timeline

import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO

interface TimelineContract {
    interface Presenter {
        fun setupTimetableMoods()
        fun getDefaultFromDate(): String
        fun getDefaultToDate(): String
        fun createDateTextFrom(dayOfMonth: Int, monthOfYear: Int, year: Int): String
        fun getSearchFromDateLong(): Long
        fun getSearchToDateLong(): Long
        fun createAddTimelineMood(): TimelineMoodBO
    }
    interface View {
        fun openEditTimelineMoodActivity(mood: TimelineMoodBO, isAddingFirstMood: Boolean = false)
        fun openTimelineMoodDetails(mood: TimelineMoodBO)
        fun setTimelineMoods(moods: List<TimelineMoodBO>)
        fun getFromDate(): String
        fun getToDate(): String
        fun showSearchEmptyView()
        fun showTimelineRecyclerView()
        fun showAddEmptyView()
    }
}