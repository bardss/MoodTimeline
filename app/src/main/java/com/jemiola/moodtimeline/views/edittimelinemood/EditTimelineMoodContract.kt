package com.jemiola.moodtimeline.views.edittimelinemood

import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import org.threeten.bp.LocalDate

interface EditTimelineMoodContract {
    interface Presenter {
        fun setupView(mood: TimelineMoodBO)
        fun getFormattedDate(date: LocalDate): String
        fun addMood()
        fun editMood()
        fun saveOpenedMoodId(id: Int?)
    }

    interface View {
        fun navigateBack()
        fun getMoodNote(): String
        fun getSelectedMood(): CircleMoodBO
        fun setupEditView(mood: TimelineMoodBO)
        fun setupAddView(date: LocalDate)
        fun getPicturePath(): String
        fun showAllDefaultViews()
    }
}