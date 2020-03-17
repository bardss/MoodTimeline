package com.jemiola.moodtimeline.addtimelineitem

import android.os.Bundle
import com.jemiola.moodtimeline.base.BaseActivity
import com.jemiola.moodtimeline.data.CircleMood
import com.jemiola.moodtimeline.data.CircleState
import com.jemiola.moodtimeline.data.ExtraKeys
import com.jemiola.moodtimeline.data.TimelineItem
import com.jemiola.moodtimeline.databinding.ActivityAddTimelineItemBinding
import kotlinx.android.synthetic.main.item_timeline.*
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.threeten.bp.LocalDate

class EditTimelineItemActivity : BaseActivity(), EditTimelineItemContract.View {

    private val presenter: EditTimelineItemContract.Presenter by inject<EditTimelineItemPresenter> {
        parametersOf(this)
    }
    private lateinit var binding: ActivityAddTimelineItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTimelineItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setupView()
    }

    private fun setupView() {
        val timelineItem = intent.getSerializableExtra(ExtraKeys.TIMELINE_ITEM) as TimelineItem
        presenter.setupView(timelineItem)
    }

    override fun setupEditView(item: TimelineItem) {
        setItemDate(item.date)
        setupMoodCircle()
    }

    override fun setupAddView(item: TimelineItem) {
        setItemDate(item.date)
        setupMoodCircle()
    }

    override fun navigateBack() {
        onBackPressed()
    }

    private fun setItemDate(date: LocalDate) {
        val formattedDate = presenter.getFormattedDate(date)
        binding.editedDayTextView.text = formattedDate
    }

    private fun setupMoodCircle() {
        binding.moodCircle.mood = CircleMood.NONE
        binding.moodCircle.state = CircleState.EDIT
    }
}