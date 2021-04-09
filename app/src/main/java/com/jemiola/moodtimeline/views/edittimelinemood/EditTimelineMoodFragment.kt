package com.jemiola.moodtimeline.views.edittimelinemood

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.Fragmenciak
import com.jemiola.moodtimeline.customviews.pickphoto.PickPhotoFragment
import com.jemiola.moodtimeline.databinding.FragmentEditTimelineMoodBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.DefaultTime
import com.jemiola.moodtimeline.utils.KeyboardUtils
import com.jemiola.moodtimeline.utils.ResUtil
import com.jemiola.moodtimeline.utils.speechtotext.SpeechToTextHandler
import com.jemiola.moodtimeline.utils.speechtotext.SpeechToTextOutput
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.threeten.bp.LocalDate

const val ANIM_DURATION = 200

class EditTimelineMoodFragment : Fragmenciak(), EditTimelineMoodContract.View, PickPhotoFragment,
    SpeechToTextOutput {

    override val a: EditTimelineMoodPresenter by inject { parametersOf(this) }
    public lateinit var binding: FragmentEditTimelineMoodBinding
    public lateinit var speechToTextHandler: SpeechToTextHandler
    public var isAddMoodOnboarding: Boolean? = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTimelineMoodBinding.inflate(inflater, container, false)
        binding.picturesLayout.setPickPhotoFragment(this)
        setUnderlineColor(R.color.colorMoodNone)
        isAddMoodOnboarding = arguments?.getBoolean(ExtraKeys.IS_ADD_MOOD_ONBOARDING, false)
        setupSpeechToNote()
        saveOpenedMoodId()
        setupView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        hideBottomMenu()
        setupOnMoodChangeAction()
    }

    override fun onBackPressed(): Boolean {
        if (isAddMoodOnboarding == false) {
            navigateBack()
        }
        return true
    }

    public fun setupSpeechToNote() {
        speechToTextHandler = SpeechToTextHandler(context, this)
        binding.microphoneImageView.setOnClickListener {
            speechToTextHandler.onClickMicrophoneIcon(context)
        }
    }

    public fun saveOpenedMoodId() {
        val mood = arguments?.getSerializable(ExtraKeys.TIMELINE_MOOD) as? TimelineMoodBOv2
        mood?.let { a.saveOpenedMoodId(it.id) }
    }

    public fun setupView() {
        if (isAddMoodOnboarding == true) {
            showOnboardingChooseMoodView()
        } else {
            val mood = arguments?.getSerializable(ExtraKeys.TIMELINE_MOOD) as? TimelineMoodBOv2
            mood?.let { a.setupView(it) }
        }
    }

    public fun setupOnMoodChangeAction() {
        binding.chooseMoodCircle.setOnSelectedMoodAction { mood ->
            binding.noteEditText.backgroundTintList =
                ResUtil.getColorAsColorStateList(resources, mood.colorId)
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
        binding.acceptImageView.setOnClickListener { a.editMood() }
        binding.acceptImageView.visibility = View.VISIBLE
        fillMoodData(mood)
    }

    override fun setupAddView(date: LocalDate) {
        changeTitle(ResUtil.getString(resources, R.string.add_note))
        binding.acceptImageView.setOnClickListener { a.addMood() }
        setMoodDate(date)
    }

    public fun fillMoodData(mood: TimelineMoodBOv2) {
        setUnderlineColor(mood.circleMood.colorId)
        setMoodDate(mood.date)
        setNote(mood.note)
        setSelectedMood(mood.circleMood)
        setSelectedImages(mood.picturesPaths)
    }

    public fun setUnderlineColor(color: Int) {
        binding.noteEditText.backgroundTintList =
            ResUtil.getColorAsColorStateList(resources, color)
    }

    public fun setSelectedImages(picturePaths: List<String>) {
        binding.picturesLayout.setPictures(picturePaths)
    }

    override fun navigateBack() {
        showBottomMenu()
        popFragment()
    }

    public fun setMoodDate(date: LocalDate) {
        val formattedDate = a.getFormattedDate(date)
        binding.editedDayTextView.text = formattedDate
    }

    public fun setNote(note: String) {
        binding.noteEditText.setText(note)
    }

    public fun setSelectedMood(mood: CircleMoodBO) {
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
            binding.microphoneImageView,
            binding.picturesLayout
        )
    }

    public fun showOnboardingChooseMoodView() {
        setMoodDate(LocalDate.now(DefaultTime.getClock()))
        changeTitle(ResUtil.getString(resources, R.string.how_do_you_feel))
        binding.acceptImageView.setOnClickListener { a.addMood() }
        AnimUtils.fadeIn(ANIM_DURATION, binding.onboardingChooseMoodImageView)
    }

    public fun setupNextButtonAddNoteOnClick() {
        binding.onboardingNextTextView.setOnClickListener {
            setupOnboardingViewAfterAddNote()
        }
    }

    public fun setupNextButtonAddPictureOnClick() {
        binding.onboardingNextTextView.setOnClickListener {
            setupOnboardingViewAfterAddPicture()
        }
    }

    public fun setupOnboardingViewAfterChooseMood() {
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
                    binding.microphoneImageView,
                    binding.onboardingAddNoteImageView,
                    binding.onboardingNextTextView
                )
            },
            binding.onboardingChooseMoodImageView
        )
    }

    public fun setupOnboardingViewAfterAddNote() {
        KeyboardUtils.hideSoftInput(activity)
        changeTitle(ResUtil.getString(resources, R.string.choose_photo))
        setupNextButtonAddPictureOnClick()
        AnimUtils.animateAlpha(
            ANIM_DURATION,
            0.5F,
            binding.chooseMoodCircle,
            binding.editedDayTextView,
            binding.noteLabelTextView,
            binding.noteEditText,
            binding.microphoneImageView
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

    public fun setupOnboardingViewAfterAddPicture() {
        changeTitle(ResUtil.getString(resources, R.string.add_note))
        AnimUtils.fadeIn(500, binding.acceptImageView)
        AnimUtils.animateAlpha(
            ANIM_DURATION,
            1F,
            binding.chooseMoodCircle,
            binding.chooseMoodCircle,
            binding.editedDayTextView,
            binding.noteLabelTextView,
            binding.noteEditText,
            binding.microphoneImageView
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

    public fun changeTitle(title: String) {
        if (binding.titleTextView.visibility == View.VISIBLE) {
            AnimUtils.fadeOut(ANIM_DURATION, binding.titleTextView)
        }
        binding.titleTextView.text = title
        AnimUtils.fadeIn(ANIM_DURATION, binding.titleTextView)
    }

    public fun scrollToBottomContentScrollView() {
        binding.contentScrollView.fullScroll(ScrollView.FOCUS_DOWN)
    }

    public fun scrollToTopContentScrollView() {
        binding.contentScrollView.fullScroll(ScrollView.FOCUS_UP)
    }

    override fun setMicrophoneIconAsNotListening() {
        val notListeningColor = ResUtil.getColor(context, R.color.colorBottomMenuActive)
        binding.microphoneImageView.setColorFilter(notListeningColor)
    }

    override fun setMicrophoneIconAsListening() {
        val listeningColor = ResUtil.getColor(context, R.color.colorMoodGood)
        binding.microphoneImageView.setColorFilter(listeningColor)
    }

    override fun setTextToInput(text: String) {
        binding.noteEditText.setText(text)
    }

    override fun getTextAlreadyInInput(): String {
        return binding.noteEditText.text.toString()
    }
}