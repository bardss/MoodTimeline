package com.jemiola.moodtimeline.base

import com.jemiola.moodtimeline.addtimelinemood.EditTimelineMoodContract
import com.jemiola.moodtimeline.addtimelinemood.EditTimelineMoodPresenter
import com.jemiola.moodtimeline.addtimelinemood.EditTimelineMoodRepository
import com.jemiola.moodtimeline.timeline.TimelineAdapterPresenter
import com.jemiola.moodtimeline.timeline.TimelineContract
import com.jemiola.moodtimeline.timeline.TimelinePresenter
import com.jemiola.moodtimeline.timeline.repository.TimelineRepository
import org.koin.dsl.module

val timelineModule = module {
    single { TimelineRepository() }
    single { TimelineAdapterPresenter() }
    factory { (view: TimelineContract.View) ->
        TimelinePresenter(view, get())
    }
}

val editTimelineItemModule = module {
    single { EditTimelineMoodRepository() }
    factory { (view: EditTimelineMoodContract.View) ->
        EditTimelineMoodPresenter(view, get())
    }
}