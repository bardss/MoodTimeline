package com.jemiola.moodtimeline.views.edittimelinemood

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.customviews.pickphoto.PickPhotoFragment
import com.jemiola.moodtimeline.databinding.FragmentEditTimelineMoodBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.DefaultTime
import com.jemiola.moodtimeline.utils.ResUtil
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.threeten.bp.LocalDate

const val ANIM_DURATION = 200

class EditTimelineMoodFragment : BaseFragment(), EditTimelineMoodContract.View, PickPhotoFragment {

    override val presenter: EditTimelineMoodPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentEditTimelineMoodBinding
    private var isAddMoodOnboarding: Boolean? = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTimelineMoodBinding.inflate(inflater, container, false)
        binding.picturesLayout.setPickPhotoFragment(this)
        setUnderlineColor(R.color.colorMoodNone)
        isAddMoodOnboarding = arguments?.getBoolean(ExtraKeys.IS_ADD_MOOD_ONBOARDING, false)
        saveOpenedMoodId()
        setupView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupOnMoodChangeAction()
    }

    override fun onBackPressed(): Boolean {
        if (isAddMoodOnboarding == false) {
            navigateBack()
        }
        return true
    }

    private fun saveOpenedMoodId() {
        val mood = arguments?.getSerializable(ExtraKeys.TIMELINE_MOOD) as? TimelineMoodBOv2
        mood?.let { presenter.saveOpenedMoodId(it.id) }
    }

    private fun setupView() {
        if (isAddMoodOnboarding == true) {
            showOnboardingChooseMoodView()
        } else {
            val mood = arguments?.getSerializable(ExtraKeys.TIMELINE_MOOD) as? TimelineMoodBOv2
            mood?.let { presenter.setupView(it) }
        }
    }

    private fun setupOnMoodChangeAction() {
        binding.chooseMoodCircle.setOnSelectedMoodAction { mood ->
            binding.noteEditText.backgroundTintList = ResUtil.getColorAsColorStateList(resources, mood.colorId)
            if (isAddMoodOnboarding == true && binding.noteEditText.visibility != View.VISIBLE) {
                setupOnboardingViewAfterChooseMood()
            } else {
                if (mood == CircleMoodBO.NONE) binding.acceptImageView.visibility = View.INVISIBLE
                else AnimUtils.fadeIn(500, binding.acceptImageView)
            }
        }
    }

    override fun setupEditView(mood: TimelineMoodBOv2) {
        changeTitle(ResUtil.getString(resources, R.string.edit_note))
        binding.acceptImageView.setOnClickListener { presenter.editMood() }
        binding.acceptImageView.visibility = View.VISIBLE
        fillMoodData(mood)
    }

    override fun setupAddView(date: LocalDate) {
        changeTitle(ResUtil.getString(resources, R.string.add_note))
        binding.acceptImageView.setOnClickListener { presenter.addMood() }
        setMoodDate(date)
    }

    private fun fillMoodData(mood: TimelineMoodBOv2) {
        setUnderlineColor(mood.circleMood.colorId)
        setMoodDate(mood.date)
        setNote(mood.note)
        setSelectedMood(mood.circleMood)
        setSelectedImages(mood.picturesPaths)
    }

    private fun setUnderlineColor(color: Int) {
        binding.noteEditText.backgroundTintList =
            ResUtil.getColorAsColorStateList(resources, color)
    }

    private fun setSelectedImages(picturePaths: List<String>) {
        binding.picturesLayout.setPictures(picturePaths)
    }

    override fun navigateBack() {
        popFragment()
    }

    private fun setMoodDate(date: LocalDate) {
        val formattedDate = presenter.getFormattedDate(date)
        binding.editedDayTextView.text = formattedDate
    }

    private fun setNote(note: String) {
        binding.noteEditText.setText(note)
    }

    private fun setSelectedMood(mood: CircleMoodBO) {
        binding.chooseMoodCircle.selectedMood = mood
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.picturesLayout.onPicturePicked(requestCode, resultCode, data)
    }

    override fun getMoodNote(): String {
        return binding.noteEditText.text.toString()
    }

    override fun getSelectedMood(): CircleMoodBO {
        return binding.chooseMoodCircle.selectedMood
    }

    override fun getPicturePaths(): List<String> {
        return binding.picturesLayout.getPicturePaths()
    }

    override fun showAllDefaultViews() {
        AnimUtils.fadeIn(
            ANIM_DURATION,
            binding.editedDayTextView,
            binding.noteLabelTextView,
            binding.noteEditText,
            binding.picturesLayout
        )
    }

    private fun showOnboardingChooseMoodView() {
        setMoodDate(LocalDate.now(DefaultTime.getClock()))
        changeTitle(ResUtil.getString(resources, R.string.how_do_you_feel))
        binding.acceptImageView.setOnClickListener { presenter.addMood() }
        AnimUtils.fadeIn(ANIM_DURATION, binding.onboardingChooseMoodImageView)
    }

    private fun setupNextButtonAddNoteOnClick() {
        binding.onboardingNextTextView.setOnClickListener {
            setupOnboardingViewAfterAddNote()
        }
    }

    private fun setupNextButtonAddPictureOnClick() {
        binding.onboardingNextTextView.setOnClickListener {
            setupOnboardingViewAfterAddPicture()
        }
    }

    private fun setupOnboardingViewAfterChooseMood() {
        changeTitle(ResUtil.getString(resources, R.string.whats_on_your_mind))
        setupNextButtonAddNoteOnClick()
        AnimUtils.animateAlpha(
            ANIM_DURATION,
            0.5F,
            binding.chooseMoodCircle
        )
        AnimUtils.fadeOut(
            ANIM_DURATION, {
                AnimUtils.fadeIn(
                    ANIM_DURATION,
                    binding.editedDayTextView,
                    binding.noteLabelTextView,
                    binding.noteEditText,
                    binding.onboardingAddNoteImageView,
                    binding.onboardingNextTextView
                )
            },
            binding.onboardingChooseMoodImageView
        )
    }

    private fun setupOnboardingViewAfterAddNote() {
        changeTitle(ResUtil.getString(resources, R.string.choose_photo))
        setupNextButtonAddPictureOnClick()
        AnimUtils.animateAlpha(
            ANIM_DURATION,
            0.5F,
            binding.chooseMoodCircle,
            binding.editedDayTextView,
            binding.noteLabelTextView,
            binding.noteEditText
        )
        AnimUtils.fadeOut(
            ANIM_DURATION, {
                AnimUtils.fadeIn(
                    ANIM_DURATION, {
                        scrollToBottomContentScrollView()
                    },
                    binding.picturesLayout,
                    binding.onboardingAddPictureImageView,
                    binding.onboardingNextTextView
                )
            },
            binding.onboardingAddNoteImageView
        )
    }

    private fun setupOnboardingViewAfterAddPicture() {
        changeTitle(ResUtil.getString(resources, R.string.add_note))
        AnimUtils.fadeIn(500, binding.acceptImageView)
        AnimUtils.animateAlpha(
            ANIM_DURATION,
            1F,
            binding.chooseMoodCircle,
            binding.chooseMoodCircle,
            binding.editedDayTextView,
            binding.noteLabelTextView,
            binding.noteEditText
        )
        scrollToTopContentScrollView()
        AnimUtils.fadeOut(
            ANIM_DURATION, {
                binding.onboardingAddPictureImageView.visibility = View.GONE
                binding.onboardingNextTextView.visibility = View.GONE
            },
            binding.onboardingAddPictureImageView,
            binding.onboardingNextTextView
        )
    }

    private fun changeTitle(title: String) {
        if (binding.titleTextView.visibility == View.VISIBLE) {
            AnimUtils.fadeOut(ANIM_DURATION, binding.titleTextView)
        }
        binding.titleTextView.text = title
        AnimUtils.fadeIn(ANIM_DURATION, binding.titleTextView)
    }

    private fun scrollToBottomContentScrollView() {
        binding.contentScrollView.fullScroll(ScrollView.FOCUS_DOWN)
    }

    private fun scrollToTopContentScrollView() {
        binding.contentScrollView.fullScroll(ScrollView.FOCUS_UP)
    }
}