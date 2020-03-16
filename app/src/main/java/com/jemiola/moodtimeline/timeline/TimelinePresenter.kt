package com.jemiola.moodtimeline.timeline

import com.jemiola.moodtimeline.data.CircleMood
import com.jemiola.moodtimeline.data.CircleState
import com.jemiola.moodtimeline.data.TimelineItem
import com.jemiola.moodtimeline.utils.DefaultClock
import com.jemiola.moodtimeline.utils.addToFront
import org.threeten.bp.LocalDate

class TimelinePresenter(
    private val view: TimelineContract.View,
    private val repository: TimelineRepository
) : TimelineContract.Presenter {

    override fun getTimelineItems(): List<TimelineItem> {
        val itemsFromRepository = repository.getDummyTimelineItems()
        return if (shouldAddItemBeVisible(itemsFromRepository)) {
            val addTimelineItem = createAddTimelineItem()
            itemsFromRepository.addToFront(addTimelineItem)
        } else itemsFromRepository
    }

    private fun createAddTimelineItem(): TimelineItem {
        return TimelineItem(
            LocalDate.now(DefaultClock.getClock()),
            "",
            CircleMood.NONE,
            CircleState.ADD
        )
    }

    private fun shouldAddItemBeVisible(items: List<TimelineItem>): Boolean {
        return items.none { it.date == LocalDate.now(DefaultClock.getClock()) }
    }

}