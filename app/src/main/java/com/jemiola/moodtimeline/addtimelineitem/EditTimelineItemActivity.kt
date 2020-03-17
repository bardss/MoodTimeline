package com.jemiola.moodtimeline.addtimelineitem

import android.os.Bundle
import com.jemiola.moodtimeline.base.BaseActivity
import com.jemiola.moodtimeline.databinding.ActivityAddTimelineItemBinding
import com.jemiola.moodtimeline.timeline.TimelineContract
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

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
}