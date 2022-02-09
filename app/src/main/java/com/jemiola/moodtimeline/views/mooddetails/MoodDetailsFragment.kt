package com.jemiola.moodtimeline.views.mooddetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jemiola.moodtimeline.base.BaseFragmentMVP
import com.jemiola.moodtimeline.databinding.FragmentDetailsTimelineMoodBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.disableFor
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.threeten.bp.LocalDate

class MoodDetailsFragment : BaseFragmentMVP(), MoodDetailsContract.View {

    override val presenter: MoodDetailsPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentDetailsTimelineMoodBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsTimelineMoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        fillMoodData()
        setupAnimations()
    }


    private fun fillMoodData() {
        val mood = arguments?.getSerializable(ExtraKeys.TIMELINE_MOOD) as? TimelineMoodBOv2
        mood?.let {
            setItemDate(mood.date)
            setNote(mood.note)
            setSelectedMood(mood.circleMood)
            setPathAsSelectedPicture(mood.picturesPaths)
        }
    }

    private fun setItemDate(date: LocalDate) {
        val formattedDate = presenter.getFormattedDate(date)
        binding.dayTextView.text = formattedDate
    }

    private fun setNote(note: String) {
        if (note.isNotEmpty()) {
            binding.noteTextView.visibility = View.VISIBLE
            binding.noteLabelTextView.visibility = View.VISIBLE
            binding.noteTextView.text = note
        }
    }

    private fun setSelectedMood(mood: CircleMoodBO) {
        binding.moodCircle.mood = mood
        binding.moodCircle.state = CircleStateBO.DEFAULT
    }

    private fun setPathAsSelectedPicture(paths: List<String>) {
        binding.picturesLayout.setPictures(paths)
    }

    private fun setupAnimations() {
        setupMoodCircleBounceAnimation()
    }

    private fun setupMoodCircleBounceAnimation() {
        val moveReset1 = {
            AnimUtils.animateMove(100, 0, binding.moodCircle)
        }
        val moveLeft2 = {
            AnimUtils.animateMove(25, 25, binding.moodCircle, moveReset1)
        }
        val moveReset2 = {
            AnimUtils.animateMove(100, 0, binding.moodCircle, moveLeft2)
        }
        val moveRight2 = {
            AnimUtils.animateMove(50, 50, binding.moodCircle, moveReset2)
        }
        val moveReset3 = {
            AnimUtils.animateMove(100, 0, binding.moodCircle, moveRight2)
        }
        val moveLeft1 = {
            AnimUtils.animateMove(100, 75, binding.moodCircle, moveReset3)
        }
        val moveReset4 = {
            AnimUtils.animateMove(100, 0, binding.moodCircle, moveLeft1)
        }
        val moveRight1 = {
            AnimUtils.animateMove(200, 100, binding.moodCircle, moveReset4)
        }
        binding.moodCircle.setOnClickListener {
            moveRight1.invoke()
            it.disableFor(1000)
        }
    }
}