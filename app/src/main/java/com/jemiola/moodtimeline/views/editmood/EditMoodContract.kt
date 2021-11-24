package com.jemiola.moodtimeline.views.editmood

import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import org.threeten.bp.LocalDate

interface EditMoodContract {
    interface Presenter {
        fun setupView(mood: TimelineMoodBOv2)
        fun getFormattedDate(date: LocalDate): String
        fun addMood()
        fun editMood()
        fun saveOpenedMoodId(id: Int?)
    }

    interface View {
        fun navigateBack()
        fun getMoodNote(): String
        fun getSelectedMood(): CircleMoodBO
        fun setupEditView(mood: TimelineMoodBOv2)
        fun setupAddView(date: LocalDate)
        fun getPicturePaths(): List<String>
        fun showAllDefaultViews()
    }
}