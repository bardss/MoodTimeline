package com.jemiola.moodtimeline.base

import com.jemiola.moodtimeline.views.edittimelinemood.EditTimelineMoodContract
import com.jemiola.moodtimeline.views.edittimelinemood.EditTimelineMoodPresenter
import com.jemiola.moodtimeline.views.edittimelinemood.EditTimelineMoodRepository
import com.jemiola.moodtimeline.views.navigation.NavigationContract
import com.jemiola.moodtimeline.views.navigation.NavigationPresenter
import com.jemiola.moodtimeline.views.navigation.NavigationRepository
import com.jemiola.moodtimeline.views.splash.SplashPresenter
import com.jemiola.moodtimeline.views.splash.SplashRepository
import com.jemiola.moodtimeline.views.timeline.TimelineAdapterPresenter
import com.jemiola.moodtimeline.views.timeline.TimelineContract
import com.jemiola.moodtimeline.views.timeline.TimelinePresenter
import com.jemiola.moodtimeline.views.timeline.TimelineRepository
import org.koin.dsl.module

val navigationModule = module {
    single { NavigationRepository() }
    factory { (view: NavigationContract.View) ->
        NavigationPresenter(view, get())
    }
}

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

val splashModule = module {
    single { SplashRepository() }
    factory { SplashPresenter(get()) }
}
