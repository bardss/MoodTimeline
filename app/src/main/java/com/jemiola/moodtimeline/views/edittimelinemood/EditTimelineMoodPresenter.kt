package com.jemiola.moodtimeline.views.edittimelinemood

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.model.data.local.CircleStateBO.*
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.utils.DefaultTime
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class EditTimelineMoodPresenter(
    private val view: EditTimelineMoodContract.View,
    override val repository: EditTimelineMoodRepository
) : BasePresenter(repository), EditTimelineMoodContract.Presenter {

    override fun saveOpenedMoodId(id: Int?) {
        repository.setOpenedMoodId(id)
    }

    override fun setupView(mood: TimelineMoodBO) {
        when (mood.circleState) {
            EDIT -> view.setupEditView(mood)
            ADD -> view.setupAddView(mood)
            DEFAULT -> view.navigateBack()
        }
    }

    override fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d").withLocale(Locale.ENGLISH)
        return date.format(formatter).capitalize()
    }

    override fun addMood() {
        val callback = createRepositoryCallback<Unit>(
            onSuccessAction = { view.navigateBack() },
            onErrorAction = {}
        )
        launch {
            val moodToAdd = createTimelineMoodBOFromView()
            repository.addMood(moodToAdd, callback)
        }
    }

    override fun editMood() {
        val callback = createRepositoryCallback<Unit>(
            onSuccessAction = { view.navigateBack() },
            onErrorAction = {}
        )
        launch {
            val moodToEdit = createTimelineMoodBOFromView()
            repository.editMood(moodToEdit, callback)
        }
    }

    private fun createTimelineMoodBOFromView(): TimelineMoodBO {
        val noteFromView = view.getMoodNote()
        val mood = view.getSelectedMood()
        val picturePath = view.getPicturePath()
        return TimelineMoodBO(
            date = LocalDate.now(DefaultTime.getClock()),
            note = noteFromView,
            circleMood = mood,
            picturePath = picturePath
        )
    }
}