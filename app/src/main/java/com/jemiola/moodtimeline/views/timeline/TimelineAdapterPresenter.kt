package com.jemiola.moodtimeline.views.timeline

import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class TimelineAdapterPresenter {

    fun onItemClick(mood: TimelineMoodBO, view: TimelineContract.View) {
        when (mood.state) {
            CircleStateBO.ADD, CircleStateBO.EDIT -> view.openEditTimelineMoodActivity(mood)
            CircleStateBO.DEFAULT -> view.openTimelineMoodDetails(mood)
        }
    }

    fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d")
        return date.format(formatter).capitalize()
    }
}