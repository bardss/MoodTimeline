package com.jemiola.moodtimeline.timeline

import com.jemiola.moodtimeline.data.CircleState
import com.jemiola.moodtimeline.data.TimelineItem
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class TimelineAdapterPresenter {

    fun onItemClick(item: TimelineItem, view: TimelineContract.View) {
        when (item.state) {
            CircleState.ADD, CircleState.EDIT -> view.openEditTimelineItemActivity()
            CircleState.DEFAULT -> view.openTimelineItemDetails()
        }
    }

    fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d")
        return date.format(formatter).capitalize()
    }
}