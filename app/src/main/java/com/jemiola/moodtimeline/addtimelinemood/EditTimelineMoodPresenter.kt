package com.jemiola.moodtimeline.addtimelinemood

import com.jemiola.moodtimeline.addtimelinemood.repository.EditTimelineMoodRepository
import com.jemiola.moodtimeline.data.local.CircleMoodBO
import com.jemiola.moodtimeline.data.local.CircleStateBO.*
import com.jemiola.moodtimeline.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.utils.DefaultClock
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class EditTimelineMoodPresenter(
    private val view: EditTimelineMoodContract.View,
    private val repository: EditTimelineMoodRepository
) : EditTimelineMoodContract.Presenter {

    override val job = Job()

    override fun setupView(mood: TimelineMoodBO) {
        when (mood.state) {
            EDIT, ADD -> view.setupEditView(mood)
            DEFAULT -> view.navigateBack()
        }
    }

    override fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d")
        return date.format(formatter).capitalize()
    }

    override fun addMood() {
        val noteFromView = view.getMoodNote()
        val moodToAdd = TimelineMoodBO(
            date = LocalDate.now(DefaultClock.getClock()),
            note = noteFromView,
            mood = CircleMoodBO.GOOD
        )
        repository.addMood(moodToAdd)
        view.navigateBack()
    }


}