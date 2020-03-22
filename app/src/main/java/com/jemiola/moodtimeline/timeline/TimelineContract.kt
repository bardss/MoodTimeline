package com.jemiola.moodtimeline.timeline

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.base.BaseView
import com.jemiola.moodtimeline.data.local.TimelineMoodBO

interface TimelineContract {
    interface Presenter : BasePresenter {
        fun setupTimelineMoods()
    }
    interface View : BaseView {
        fun openEditTimelineMoodActivity(mood: TimelineMoodBO)
        fun openTimelineMoodDetails(mood: TimelineMoodBO)
        fun setTimelineMoods(moods: List<TimelineMoodBO>)
    }
}