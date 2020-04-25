package com.jemiola.moodtimeline.views.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentTimelineBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.PermissionsUtil
import com.jemiola.moodtimeline.utils.ResUtil
import com.jemiola.moodtimeline.views.detailstimelinemood.DetailsTimelineMoodFragment
import com.jemiola.moodtimeline.views.edittimelinemood.EditTimelineMoodFragment
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class TimelineFragment : BaseFragment(), TimelineContract.View {

    override val presenter: TimelinePresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentTimelineBinding
    private var isSearchOpened = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimelineBinding.inflate(inflater, container, false)
        setupStoragePermissions()
        setupTimeline()
        setupSearchOptions()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        presenter.refreshTimelineMoods()
    }

    private fun setupStoragePermissions() {
        if (!PermissionsUtil.isStoragePermissionGranted()) {
            PermissionsUtil.askForStoragePermission()
        }
    }

    private fun setupTimeline() {
        with(binding.timelineRecyclerView) {
            adapter = TimelineAdapter(this@TimelineFragment)
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun setTimelineMoods(moods: List<TimelineMoodBO>) {
        val adapter = binding.timelineRecyclerView.adapter
        (adapter as TimelineAdapter).setTimelineMoods(moods)
    }

    override fun openEditTimelineMoodActivity(mood: TimelineMoodBO) {
        val editTimelineMoodFragment = EditTimelineMoodFragment()
        editTimelineMoodFragment.arguments = createBundleWithTimelineMood(mood)
        pushFragment(editTimelineMoodFragment)
    }

    override fun openTimelineMoodDetails(mood: TimelineMoodBO) {
        val detailsTimelineMoodFragment = DetailsTimelineMoodFragment()
        detailsTimelineMoodFragment.arguments = createBundleWithTimelineMood(mood)
        pushFragment(detailsTimelineMoodFragment)
    }

    private fun createBundleWithTimelineMood(mood: TimelineMoodBO): Bundle {
        return Bundle().apply {
            putSerializable(ExtraKeys.TIMELINE_MOOD, mood)
        }
    }

    private fun setupSearchOptions() {
        binding.fromEditText.backgroundTintList =
            ResUtil.getColorAsColorStateList(R.color.colorTitle)
        binding.fromEditText.setHintTextColor(ResUtil.getColorAsColorStateList(R.color.colorMoodNone))
        binding.toEditText.backgroundTintList = ResUtil.getColorAsColorStateList(R.color.colorTitle)
        binding.toEditText.setHintTextColor(ResUtil.getColorAsColorStateList(R.color.colorMoodNone))
        binding.topBarLayout.post {
            initialSearchLayoutMoveOutOfScreen()
            binding.searchImageView.setOnClickListener { onSearchClick() }
        }
    }

    private fun onSearchClick() {
        val distance = binding.topBarLayout.width
        val searchIconWidth = binding.searchImageView.width
        val timelineLayoutPadding = binding.timelineLayout.paddingStart
        if (!isSearchOpened) {
            isSearchOpened = true
            val hideDistance = -distance + searchIconWidth + timelineLayoutPadding
            AnimUtils.animateMove(500, hideDistance, binding.topBarLayout)
            AnimUtils.animateMove(500, 0, binding.searchLayout)
        } else {
            isSearchOpened = false
            AnimUtils.animateMove(500, 0, binding.topBarLayout)
            AnimUtils.animateMove(500, distance, binding.searchLayout)
        }
    }

    private fun initialSearchLayoutMoveOutOfScreen() {
        val distance = binding.topBarLayout.width
        AnimUtils.animateMove(500, distance, binding.searchLayout) {
            binding.searchLayout.visibility = View.VISIBLE
        }
    }

}