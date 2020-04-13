package com.jemiola.moodtimeline.views.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentTimelineBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.utils.PermissionsUtil
import com.jemiola.moodtimeline.views.edittimelinemood.EditTimelineMoodFragment
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class TimelineFragment : BaseFragment(), TimelineContract.View {

    override val presenter: TimelinePresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentTimelineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimelineBinding.inflate(inflater, container, false)
        setupStoragePermissions()
        setupTimeline()
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
        editTimelineMoodFragment.arguments = createEditTimelineMoodBundle(mood)
        pushFragment(editTimelineMoodFragment)
    }

    private fun createEditTimelineMoodBundle(mood: TimelineMoodBO) : Bundle{
        return Bundle().apply {
            putSerializable(ExtraKeys.TIMELINE_MOOD, mood)
        }
    }

    override fun openTimelineMoodDetails(mood: TimelineMoodBO) {
    }
}