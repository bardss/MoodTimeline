package com.jemiola.moodtimeline.views.addtimelinemood

import android.os.Bundle
import com.jemiola.moodtimeline.base.BaseActivity
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.databinding.ActivityEditTimelineMoodBinding
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.threeten.bp.LocalDate

class EditTimelineMoodActivity : BaseActivity(), EditTimelineMoodContract.View {

    override val presenter: EditTimelineMoodPresenter by inject { parametersOf(this) }
    private lateinit var binding: ActivityEditTimelineMoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTimelineMoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setupView()
        setupButtons()
    }

    private fun setupButtons() {
        binding.acceptImageView.setOnClickListener {
            presenter.addMood()
        }
    }

    private fun setupView() {
        val timelineItem = intent.getSerializableExtra(ExtraKeys.TIMELINE_MOOD) as TimelineMoodBO
        presenter.setupView(timelineItem)
    }

    override fun setupEditView(mood: TimelineMoodBO) {
        setItemDate(mood.date)
        setupMoodCircle()
    }

    override fun navigateBack() {
        onBackPressed()
    }

    override fun getMoodNote(): String {
        return binding.noteEditText.text.toString()
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