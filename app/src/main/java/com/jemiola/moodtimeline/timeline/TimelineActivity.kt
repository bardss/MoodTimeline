package com.jemiola.moodtimeline.timeline

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jemiola.moodtimeline.addtimelinemood.EditTimelineMoodActivity
import com.jemiola.moodtimeline.base.BaseActivity
import com.jemiola.moodtimeline.data.ExtraKeys
import com.jemiola.moodtimeline.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.databinding.ActivityTimelineBinding
import com.jemiola.moodtimeline.timeline.repository.TimelineRepository
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class TimelineActivity : BaseActivity(), TimelineContract.View {

    private val presenter: TimelineContract.Presenter by inject<TimelinePresenter> {
        parametersOf(this, TimelineRepository())
    }
    private lateinit var binding: ActivityTimelineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimelineBinding.inflate(layoutInflater)
        setupTimeline()
        presenter.setupTimelineMoods()
        setContentView(binding.root)
    }

    private fun setupTimeline() {
        binding.timelineRecyclerView.adapter = TimelineAdapter(this)
        binding.timelineRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun setTimelineMoods(moods: List<TimelineMoodBO>) {
        val adapter = binding.timelineRecyclerView.adapter
        (adapter as TimelineAdapter).setTimelineMoods(moods)
    }

    override fun openEditTimelineMoodActivity(mood: TimelineMoodBO) {
        startActivity(
            Intent(this, EditTimelineMoodActivity::class.java)
                .putExtra(ExtraKeys.TIMELINE_MOOD, mood)
        )
    }

    override fun openTimelineMoodDetails(mood: TimelineMoodBO) {
    }
}