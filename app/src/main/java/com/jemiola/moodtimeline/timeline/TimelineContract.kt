package com.jemiola.moodtimeline.timeline

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.base.BaseView
import com.jemiola.moodtimeline.data.TimelineItem

interface TimelineContract {
    interface Presenter : BasePresenter {
        fun getTimelineItems(): List<TimelineItem>
    }
    interface View : BaseView
}