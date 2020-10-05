package com.jemiola.moodtimeline.views.timeline

import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2

interface TimelineContract {
    interface Presenter {
        fun setupTimetableMoods()
        fun getDefaultFromDate(): String
        fun getDefaultToDate(): String
        fun createDateTextFrom(dayOfMonth: Int, monthOfYear: Int, year: Int): String
        fun getSearchFromDateLong(): Long
        fun getSearchToDateLong(): Long
        fun createAddTimelineMood(): TimelineMoodBOv2
    }

    interface View {
        fun openEditTimelineMoodActivity(mood: TimelineMoodBOv2, isAddingFirstMood: Boolean = false)
        fun openTimelineMoodDetails(mood: TimelineMoodBOv2)
        fun setTimelineMoods(moods: List<TimelineMoodBOv2>)
        fun getFromDate(): String
        fun getToDate(): String
        fun showSearchEmptyView()
        fun showTimelineRecyclerView()
        fun showAddEmptyView()
        fun setupComeBackLaterView()
    }
}