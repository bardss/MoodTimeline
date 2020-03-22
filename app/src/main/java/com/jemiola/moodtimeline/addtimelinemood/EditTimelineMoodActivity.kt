package com.jemiola.moodtimeline.addtimelinemood

import android.os.Bundle
import com.jemiola.moodtimeline.base.BaseActivity
import com.jemiola.moodtimeline.data.local.CircleMoodBO
import com.jemiola.moodtimeline.data.local.CircleStateBO
import com.jemiola.moodtimeline.data.ExtraKeys
import com.jemiola.moodtimeline.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.databinding.ActivityAddTimelineItemBinding
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.threeten.bp.LocalDate

class EditTimelineMoodActivity : BaseActivity(), EditTimelineMoodContract.View {

    private val presenter: EditTimelineMoodContract.Presenter by inject<EditTimelineMoodPresenter> {
        parametersOf(this)
    }
    private lateinit var binding: ActivityAddTimelineItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTimelineItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setupView()
    }

    private fun setupView() {
        val timelineItem = intent.getSerializableExtra(ExtraKeys.TIMELINE_MOOD) as TimelineMoodBO
        presenter.setupView(timelineItem)
    }

    override fun setupEditView(mood: TimelineMoodBO) {
        setItemDate(mood.date)
        setupMoodCircle()
    }

    override fun setupAddView(mood: TimelineMoodBO) {
        setItemDate(mood.date)
        setupMoodCircle()
    }

    override fun navigateBack() {
        onBackPressed()
    }

    private fun setItemDate(date: LocalDate) {
        val formattedDate = presenter.getFormattedDate(date)
        binding.editedDayTextView.text = formattedDate
    }

    private fun setupMoodCircle() {
        binding.moodCircle.mood = CircleMoodBO.NONE
        binding.moodCircle.state = CircleStateBO.EDIT
    }
}