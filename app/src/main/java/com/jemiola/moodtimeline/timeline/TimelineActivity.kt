package com.jemiola.moodtimeline.timeline

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jemiola.moodtimeline.base.BaseActivity
import com.jemiola.moodtimeline.databinding.ActivityTimelineBinding
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class TimelineActivity : BaseActivity(), TimelineContract.View {

    private val presenter: TimelineContract.Presenter by inject<TimelinePresenter> {
        parametersOf(this)
    }
    private lateinit var binding: ActivityTimelineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimelineBinding.inflate(layoutInflater)
        setupTimeline()
        setDummyTimelineNotes()
        setContentView(binding.root)
    }

    private fun setupTimeline() {
        binding.timelineRecyclerView.adapter = TimelineAdapter()
        binding.timelineRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setDummyTimelineNotes() {
        val items = presenter.getTimelineItems()
        val adapter = binding.timelineRecyclerView.adapter
        (adapter as TimelineAdapter).setItems(items)
    }
}