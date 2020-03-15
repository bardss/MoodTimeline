package com.jemiola.moodtimeline.base

import com.jemiola.moodtimeline.timeline.TimelineContract
import com.jemiola.moodtimeline.timeline.TimelinePresenter
import com.jemiola.moodtimeline.timeline.TimelineRepository
import org.koin.dsl.module


val timelineModule = module {
    single { TimelineRepository() }
    factory { (view: TimelineContract.View) ->
        TimelinePresenter(view, get())
    }
}