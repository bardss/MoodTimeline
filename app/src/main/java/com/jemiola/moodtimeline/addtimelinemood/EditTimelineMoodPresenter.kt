package com.jemiola.moodtimeline.addtimelinemood

import com.jemiola.moodtimeline.data.local.CircleStateBO.*
import com.jemiola.moodtimeline.data.local.TimelineMoodBO
import kotlinx.coroutines.Job
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
            ADD -> view.setupAddView(mood)
            DEFAULT -> view.navigateBack()
        }
    }

    override fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d")
        return date.format(formatter).capitalize()
    }

}