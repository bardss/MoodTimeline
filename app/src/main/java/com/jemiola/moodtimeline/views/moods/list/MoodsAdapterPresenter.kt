package com.jemiola.moodtimeline.views.moods.list

import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class MoodsAdapterPresenter {

    fun onItemClick(mood: TimelineMoodBOv2, actions: MoodClickActions) {
        when (mood.circleState) {
            CircleStateBO.ADD, CircleStateBO.EDIT -> actions.openEditTimelineMoodActivity(mood)
            CircleStateBO.DEFAULT -> actions.openTimelineMoodDetails(mood)
        }
    }

    fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d").withLocale(Locale.ENGLISH)
        return date.format(formatter).capitalize()
    }
}