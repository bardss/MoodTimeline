package com.jemiola.moodtimeline.base

import com.jemiola.moodtimeline.addtimelineitem.EditTimelineItemContract
import com.jemiola.moodtimeline.addtimelineitem.EditTimelineItemPresenter
import com.jemiola.moodtimeline.addtimelineitem.EditTimelineItemRepository
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

val addTimelineItemModule = module {
    single { EditTimelineItemRepository() }
    factory { (view: EditTimelineItemContract.View) ->
        EditTimelineItemPresenter(view, get())
    }
}