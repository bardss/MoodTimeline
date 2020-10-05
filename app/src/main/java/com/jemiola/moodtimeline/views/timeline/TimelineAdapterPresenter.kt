package com.jemiola.moodtimeline.views.timeline

import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class TimelineAdapterPresenter {

    fun onItemClick(mood: TimelineMoodBOv2, view: TimelineContract.View) {
        when (mood.circleState) {
            CircleStateBO.ADD, CircleStateBO.EDIT -> view.openEditTimelineMoodActivity(mood)
            CircleStateBO.DEFAULT -> view.openTimelineMoodDetails(mood)
        }
    }

    fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d").withLocale(Locale.ENGLISH)
        return date.format(formatter).capitalize()
    }
}