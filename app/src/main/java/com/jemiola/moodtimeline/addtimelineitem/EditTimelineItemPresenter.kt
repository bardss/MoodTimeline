package com.jemiola.moodtimeline.addtimelineitem

import com.jemiola.moodtimeline.data.CircleState.*
import com.jemiola.moodtimeline.data.TimelineItem
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class EditTimelineItemPresenter(
    private val view: EditTimelineItemContract.View,
    private val repository: EditTimelineItemRepository
) : EditTimelineItemContract.Presenter {

    override fun setupView(item: TimelineItem) {
        when (item.state) {
            EDIT, ADD -> view.setupEditView(item)
            ADD -> view.setupAddView(item)
            DEFAULT -> view.navigateBack()
        }
    }

    override fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d")
        return date.format(formatter).capitalize()
    }

}