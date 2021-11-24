package com.jemiola.moodtimeline.views.moods.timeline

import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2

interface TimelineContract {
    interface Presenter {
        fun setupTimetableMoods()
        fun createAddTimelineMood(): TimelineMoodBOv2
    }

    interface View {
        fun setPagedTimelineMoods(moods: List<TimelineMoodBOv2>)
        fun showAddEmptyView()
        fun setupComeBackLaterView()
        fun showBottomMenu()
    }
}