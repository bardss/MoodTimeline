package com.jemiola.moodtimeline.timeline

import android.os.Bundle
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseActivity
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class TimelineActivity: BaseActivity(), TimelineContract.View {
    private val presenter: TimelinePresenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)
    }
}