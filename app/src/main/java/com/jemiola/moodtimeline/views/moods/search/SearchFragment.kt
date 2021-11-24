package com.jemiola.moodtimeline.views.moods.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.databinding.FragmentSearchBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.ResUtil
import com.jemiola.moodtimeline.utils.rangepickers.RangePickersUtil
import com.jemiola.moodtimeline.utils.viewpager.ViewPagerChildFragment
import com.jemiola.moodtimeline.views.editmood.EditMoodFragment
import com.jemiola.moodtimeline.views.mooddetails.MoodDetailsFragment
import com.jemiola.moodtimeline.views.moods.MoodClickActions
import com.jemiola.moodtimeline.views.moods.timeline.EMPTY_VIEW_ANIM_DURATION
import com.jemiola.moodtimeline.views.moods.timeline.TimelineAdapter
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class SearchFragment : ViewPagerChildFragment(), SearchContract.View, MoodClickActions {

    override val presenter: SearchPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentSearchBinding
    private val rangePickersUtil = RangePickersUtil()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentSearchBinding.inflate(inflater, container, false)
            setupTimeline()
            setupCloseSearchClick()
            setupSearchView()
        }
        return binding.root
    }

    private fun setupTimeline() {
        with(binding.timelineRecyclerView) {
            adapter = TimelineAdapter(this@SearchFragment)
            val recyclerViewManager = LinearLayoutManager(context)
            layoutManager = recyclerViewManager
            layoutAnimation =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
            scheduleLayoutAnimation()
        }
    }

    private fun setupCloseSearchClick() {
        binding.closeSearchImageView.setOnClickListener {
            swipeViewPagerLeft()
        }
    }

    private fun setupSearchView() {
        setupSearchDefaultValues()
        setupSearchEditTextColors()
        setupSearchCalendars()
        presenter.searchTimelineMoods()
    }

    private fun setupSearchCalendars() {
        context?.let {
            val fromEditText = binding.fromEditText
            val toEditText = binding.toEditText
            rangePickersUtil.setupRangeCalendars(
                it,
                fromEditText,
                toEditText
            ) { presenter.searchTimelineMoods() }
        }
    }


    private fun setupSearchDefaultValues() {
        val fromDate = presenter.getDefaultFromDate()
        val toDate = presenter.getDefaultToDate()
        binding.fromEditText.setText(fromDate)
        binding.toEditText.setText(toDate)
    }

    private fun setupSearchEditTextColors() {
        binding.fromEditText.backgroundTintList =
            ResUtil.getColorAsColorStateList(resources, R.color.colorTitle)
        binding.fromEditText.setHintTextColor(
            ResUtil.getColorAsColorStateList(
                resources,
                R.color.colorMoodNone
            )
        )
        binding.toEditText.backgroundTintList =
            ResUtil.getColorAsColorStateList(resources, R.color.colorTitle)
        binding.toEditText.setHintTextColor(
            ResUtil.getColorAsColorStateList(
                resources,
                R.color.colorMoodNone
            )
        )
    }

    override fun openEditTimelineMoodActivity(mood: TimelineMoodBOv2, isAddingFirstMood: Boolean) {
        val editTimelineMoodFragment = EditMoodFragment()
        editTimelineMoodFragment.arguments = createBundleEditTimelineMood(mood, isAddingFirstMood)
        pushFragment(editTimelineMoodFragment)
    }

    override fun openTimelineMoodDetails(mood: TimelineMoodBOv2) {
        val detailsTimelineMoodFragment = MoodDetailsFragment()
        detailsTimelineMoodFragment.arguments = createBundleDetailsTimelineMood(mood)
        pushFragment(detailsTimelineMoodFragment)
    }

    private fun createBundleEditTimelineMood(
        mood: TimelineMoodBOv2,
        isAddingFirstMood: Boolean
    ): Bundle {
        return Bundle().apply {
            putSerializable(ExtraKeys.TIMELINE_MOOD, mood)
            putBoolean(ExtraKeys.IS_ADD_MOOD_ONBOARDING, isAddingFirstMood)
        }
    }

    private fun createBundleDetailsTimelineMood(mood: TimelineMoodBOv2): Bundle {
        return Bundle().apply {
            putSerializable(ExtraKeys.TIMELINE_MOOD, mood)
        }
    }

    override fun getFromDate(): String {
        return binding.fromEditText.text.toString()
    }

    override fun getToDate(): String {
        return binding.toEditText.text.toString()
    }

    override fun setMoods(moods: List<TimelineMoodBOv2>) {
        val adapter = binding.timelineRecyclerView.adapter
        (adapter as TimelineAdapter).setTimelineMoods(moods)
    }

    override fun showSearchEmptyView() {
        hideViewWhenVisible(binding.timelineRecyclerView)
        showViewWhenHidden(binding.searchEmptyViewLayout)
    }

    override fun showTimelineRecyclerView() {
        showViewWhenHidden(binding.timelineRecyclerView)
        hideViewWhenVisible(binding.searchEmptyViewLayout)
    }

    private fun showViewWhenHidden(view: View) {
        if (view.visibility == View.GONE || view.visibility == View.INVISIBLE) {
            AnimUtils.fadeIn(EMPTY_VIEW_ANIM_DURATION, { view.visibility = View.VISIBLE }, view)
        }
    }

    private fun hideViewWhenVisible(view: View) {
        if (view.visibility != View.GONE) {
            AnimUtils.fadeOut(EMPTY_VIEW_ANIM_DURATION, { view.visibility = View.GONE }, view)
        }
    }
}