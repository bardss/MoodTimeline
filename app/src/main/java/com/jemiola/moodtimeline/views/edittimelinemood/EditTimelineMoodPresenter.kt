package com.jemiola.moodtimeline.views.edittimelinemood

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO.*
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.utils.DefaultClock
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class EditTimelineMoodPresenter(
    private val view: EditTimelineMoodContract.View,
    override val repository: EditTimelineMoodRepository
) : BasePresenter(repository), EditTimelineMoodContract.Presenter {

    override fun setupView(mood: TimelineMoodBO) {
        when (mood.circleState) {
            EDIT, ADD -> view.setupEditView(mood)
            DEFAULT -> view.navigateBack()
        }
    }

    override fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d")
        return date.format(formatter).capitalize()
    }

    override fun addMood() {
        launch {
            val noteFromView = view.getMoodNote()
            val moodToAdd = TimelineMoodBO(
                date = LocalDate.now(DefaultClock.getClock()),
                note = noteFromView,
                circleMood = CircleMoodBO.GOOD
            )
            repository.addMood(moodToAdd)
        }
        view.navigateBack()
    }


}