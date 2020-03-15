package com.jemiola.moodtimeline.timeline

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jemiola.moodtimeline.base.BaseActivity
import com.jemiola.moodtimeline.data.CircleMood
import com.jemiola.moodtimeline.data.TimelineItem
import com.jemiola.moodtimeline.databinding.ActivityTimelineBinding
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.threeten.bp.LocalDate

class TimelineActivity: BaseActivity(), TimelineContract.View {

    private val presenter: TimelinePresenter by inject { parametersOf(this) }
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
        val items = listOf(
            TimelineItem(
                LocalDate.now(),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                CircleMood.GOOD
            ),
            TimelineItem(
                LocalDate.now().minusDays(1),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                CircleMood.GOOD
            ),
            TimelineItem(
                LocalDate.now().minusDays(2),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                CircleMood.BAD
            ),
            TimelineItem(
                LocalDate.now().minusDays(3),
                "Lorem ipsum",
                CircleMood.BAD
            ),
            TimelineItem(
                LocalDate.now().minusDays(4),
                "Lorem ipsum dolor sit amet",
                CircleMood.MEDIOCRE
            ),
            TimelineItem(
                LocalDate.now().minusDays(5),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                CircleMood.GOOD
            )
        )
        val adapter = binding.timelineRecyclerView.adapter
        (adapter as TimelineAdapter).setItems(items)
    }
}