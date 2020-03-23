package com.jemiola.moodtimeline.views.timeline

import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO

interface TimelineContract {
    interface Presenter {
        fun refreshTimelineMoods()
    }
    interface View {
        fun openEditTimelineMoodActivity(mood: TimelineMoodBO)
        fun openTimelineMoodDetails(mood: TimelineMoodBO)
        fun setTimelineMoods(moods: List<TimelineMoodBO>)
    }
}