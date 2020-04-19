package com.jemiola.moodtimeline.views.detailstimelinemood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentDetailsTimelineMoodBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.utils.ImageUtils
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.threeten.bp.LocalDate

const val PICTURE_QUALITY = 60

class DetailsTimelineMoodFragment : BaseFragment(), DetailsTimelineMoodContract.View {

    override val presenter: DetailsTimelineMoodPresenter by inject { parametersOf(this) }
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

}