package com.jemiola.moodtimeline.views.edittimelinemood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentEditTimelineMoodBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.threeten.bp.LocalDate

class EditTimelineMoodFragment : BaseFragment(), EditTimelineMoodContract.View {

    override val presenter: EditTimelineMoodPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentEditTimelineMoodBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTimelineMoodBinding.inflate(inflater, container, false)
        return binding.root
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
        (arguments?.getSerializable(ExtraKeys.TIMELINE_MOOD) as? TimelineMoodBO)?.let {
            presenter.setupView(it)
        }
    }

    override fun setupEditView(mood: TimelineMoodBO) {
        setItemDate(mood.date)
        setupMoodCircle()
    }

    override fun navigateBack() {
        popFragment()
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