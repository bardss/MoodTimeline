package com.jemiola.moodtimeline.views.calendar

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.views.timeline.TimelineContract
import com.jemiola.moodtimeline.views.timeline.TimelineRepository

class CalendarPresenter(
    private val view: CalendarContract.View,
    override val repository: CalendarRepository
) : BasePresenter(repository), CalendarContract.Presenter {

}