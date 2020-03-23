package com.jemiola.moodtimeline.views.addtimelinemood

import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import org.threeten.bp.LocalDate

interface EditTimelineMoodContract {
    interface Presenter {
        fun setupView(mood: TimelineMoodBO)
        fun getFormattedDate(date: LocalDate): String
        fun addMood()
    }

    interface View {
        fun setupEditView(mood: TimelineMoodBO)
        fun navigateBack()
        fun getMoodNote(): String
    }
}