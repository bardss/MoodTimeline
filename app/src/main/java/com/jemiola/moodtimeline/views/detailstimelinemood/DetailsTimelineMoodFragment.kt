package com.jemiola.moodtimeline.views.detailstimelinemood

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentDetailsTimelineMoodBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.ImageUtils
import com.jemiola.moodtimeline.utils.disableFor
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.threeten.bp.LocalDate

const val PICTURE_QUALITY = 60

class DetailsTimelineMoodFragment : BaseFragment(), DetailsTimelineMoodContract.View {

    override val presenter: DetailsTimelineMoodPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentDetailsTimelineMoodBinding
    private var pictureHighlighted = false

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
        val mood = arguments?.getSerializable(ExtraKeys.TIMELINE_MOOD) as? TimelineMoodBO
        mood?.let {
            setItemDate(mood.date)
            setNote(mood.note)
            setSelectedMood(mood.circleMood)
            setPathAsSelectedPicture(mood.picturePath)
        }
    }

    private fun setItemDate(date: LocalDate) {
        val formattedDate = presenter.getFormattedDate(date)
        binding.dayTextView.text = formattedDate
    }

    private fun setNote(note: String) {
        binding.noteTextView.text = note
    }

    private fun setSelectedMood(mood: CircleMoodBO) {
        binding.moodCircle.mood = mood
    }

    private fun setPathAsSelectedPicture(path: String?) {
        val pictureBitmap = ImageUtils.getBitmapDrawableFromPath(path, PICTURE_QUALITY)
        if (pictureBitmap != null) {
            binding.selectedPictureImageView.setImageDrawable(pictureBitmap)
        }
    }

    private fun setupAnimations() {
        setupMoodCircleBounceAnimation()
        setupPictureAlphaAnimation()
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setupPictureAlphaAnimation() {
        val duration = 250
        binding.selectedPictureImageView.setOnTouchListener { view, event ->
            when (event.action) {
                ACTION_DOWN -> onPictureActionDown(duration, view)
                ACTION_UP -> onPictureActionUp(duration, view)
            }
            view.disableFor(duration)
            true
        }
    }

    private fun onPictureActionUp(duration: Int, view: View) {
        if (pictureHighlighted) AnimUtils.animateAlpha(duration, 1f, 0.7f, view)
        pictureHighlighted = false
    }

    private fun onPictureActionDown(duration: Int, view: View) {
        if (!pictureHighlighted) AnimUtils.animateAlpha(duration, 0.7f, 1f, view)
        pictureHighlighted = true
        Handler().postDelayed({ onPictureActionUp(duration, view) }, 3000)
    }

}