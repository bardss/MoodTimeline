package com.jemiola.moodtimeline.views.edittimelinemood

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentEditTimelineMoodBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.ResUtil
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
        setUnderlineColor(R.color.colorMoodNone)
        binding.pickPhotoView.setFragment(this)
        saveOpenedMoodId()
        setupView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupOnMoodChangeAction()
    }

    override fun onBackPressed(): Boolean {
        val isAddingFirstMood =  arguments?.getBoolean(ExtraKeys.IS_ADDING_FIRST_MOOD, false)
        if (isAddingFirstMood == false) {
            navigateBack()
        }
        return true
    }

    private fun saveOpenedMoodId() {
        val mood = arguments?.getSerializable(ExtraKeys.TIMELINE_MOOD) as? TimelineMoodBO
        mood?.let { presenter.saveOpenedMoodId(it.id) }
    }

    private fun setupView() {
        val mood = arguments?.getSerializable(ExtraKeys.TIMELINE_MOOD) as? TimelineMoodBO
        mood?.let { presenter.setupView(it) }
    }

    private fun setupOnMoodChangeAction() {
        binding.chooseMoodCircle.setOnSelectedMoodAction { mood ->
            if (mood == CircleMoodBO.NONE) binding.acceptImageView.visibility = View.INVISIBLE
            else AnimUtils.fadeIn(500, binding.acceptImageView)
            binding.noteEditText.backgroundTintList = ResUtil.getColorAsColorStateList(mood.colorId)
        }
    }

    override fun setupEditView(mood: TimelineMoodBO) {
        binding.titleTextView.text = ResUtil.getString(R.string.edit_note)
        binding.acceptImageView.setOnClickListener { presenter.editMood() }
        binding.acceptImageView.visibility = View.VISIBLE
        fillMoodData(mood)
    }

    override fun setupAddView(mood: TimelineMoodBO) {
        binding.titleTextView.text = ResUtil.getString(R.string.add_note)
        binding.acceptImageView.setOnClickListener { presenter.addMood() }
        setItemDate(mood.date)
    }

    private fun fillMoodData(mood: TimelineMoodBO) {
        setUnderlineColor(mood.circleMood.colorId)
        setItemDate(mood.date)
        setNote(mood.note)
        setSelectedMood(mood.circleMood)
        setSelectedImage(mood.picturePath)
    }

    private fun setUnderlineColor(color: Int) {
        binding.noteEditText.backgroundTintList =
            ResUtil.getColorAsColorStateList(color)
    }

    private fun setSelectedImage(picturePath: String) {
        binding.pickPhotoView.setPathAsSelectedPicture(picturePath)
    }

    override fun navigateBack() {
        popFragment()
    }

    private fun setItemDate(date: LocalDate) {
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
        binding.pickPhotoView.onActivityResult(requestCode, resultCode, data)
    }

    override fun getMoodNote(): String {
        return binding.noteEditText.text.toString()
    }

    override fun getSelectedMood(): CircleMoodBO {
        return binding.chooseMoodCircle.selectedMood
    }

    override fun getPicturePath(): String {
        return binding.pickPhotoView.picturePath ?: ""
    }
}