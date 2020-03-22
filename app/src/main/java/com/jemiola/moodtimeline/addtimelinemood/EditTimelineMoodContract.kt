package com.jemiola.moodtimeline.addtimelinemood

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.base.BaseView
import com.jemiola.moodtimeline.data.local.TimelineMoodBO
import org.threeten.bp.LocalDate

interface EditTimelineMoodContract {
    interface Presenter : BasePresenter {
        fun setupView(mood: TimelineMoodBO)
        fun getFormattedDate(date: LocalDate): String
    }

    interface View : BaseView {
        fun setupEditView(mood: TimelineMoodBO)
        fun setupAddView(mood: TimelineMoodBO)
        fun navigateBack()
    }
}