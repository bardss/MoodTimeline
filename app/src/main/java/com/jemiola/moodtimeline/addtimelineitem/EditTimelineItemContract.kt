package com.jemiola.moodtimeline.addtimelineitem

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.base.BaseView
import com.jemiola.moodtimeline.data.TimelineItem
import org.threeten.bp.LocalDate

interface EditTimelineItemContract {
    interface Presenter : BasePresenter {
        fun setupView(item: TimelineItem)
        fun getFormattedDate(date: LocalDate): String
    }

    interface View : BaseView {
        fun setupEditView(item: TimelineItem)
        fun setupAddView(item: TimelineItem)
        fun navigateBack()
    }
}