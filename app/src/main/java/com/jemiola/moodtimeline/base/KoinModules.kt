package com.jemiola.moodtimeline.base

import com.jemiola.moodtimeline.addtimelineitem.EditTimelineItemContract
import com.jemiola.moodtimeline.addtimelineitem.EditTimelineItemPresenter
import com.jemiola.moodtimeline.addtimelineitem.EditTimelineItemRepository
import com.jemiola.moodtimeline.timeline.TimelineAdapterPresenter
import com.jemiola.moodtimeline.timeline.TimelineContract
import com.jemiola.moodtimeline.timeline.TimelinePresenter
import com.jemiola.moodtimeline.timeline.TimelineRepository
import org.koin.dsl.module

val timelineModule = module {
    single { TimelineRepository() }
    single { TimelineAdapterPresenter() }
    factory { (view: TimelineContract.View) ->
        TimelinePresenter(view, get())
    }
}

val editTimelineItemModule = module {
    single { EditTimelineItemRepository() }
    factory { (view: EditTimelineItemContract.View) ->
        EditTimelineItemPresenter(view, get())
    }
}