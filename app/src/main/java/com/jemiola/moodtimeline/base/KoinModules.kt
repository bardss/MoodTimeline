package com.jemiola.moodtimeline.base

import com.jemiola.moodtimeline.views.calendar.CalendarContract
import com.jemiola.moodtimeline.views.calendar.CalendarPresenter
import com.jemiola.moodtimeline.views.calendar.CalendarRepository
import com.jemiola.moodtimeline.views.detailstimelinemood.DetailsTimelineMoodContract
import com.jemiola.moodtimeline.views.detailstimelinemood.DetailsTimelineMoodPresenter
import com.jemiola.moodtimeline.views.detailstimelinemood.DetailsTimelineMoodRepository
import com.jemiola.moodtimeline.views.edittimelinemood.EditTimelineMoodContract
import com.jemiola.moodtimeline.views.edittimelinemood.EditTimelineMoodPresenter
import com.jemiola.moodtimeline.views.edittimelinemood.EditTimelineMoodRepository
import com.jemiola.moodtimeline.views.navigation.NavigationContract
import com.jemiola.moodtimeline.views.navigation.NavigationPresenter
import com.jemiola.moodtimeline.views.navigation.NavigationRepository
import com.jemiola.moodtimeline.views.settings.SettingsContract
import com.jemiola.moodtimeline.views.settings.SettingsPresenter
import com.jemiola.moodtimeline.views.settings.SettingsRepository
import com.jemiola.moodtimeline.views.settings.generatepdf.GeneratePdfContract
import com.jemiola.moodtimeline.views.settings.generatepdf.GeneratePdfPresenter
import com.jemiola.moodtimeline.views.settings.generatepdf.GeneratePdfRepository
import com.jemiola.moodtimeline.views.splash.SplashContract
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

val detailsTimelineItemModule = module {
    single { DetailsTimelineMoodRepository() }
    factory { (view: DetailsTimelineMoodContract.View) ->
        DetailsTimelineMoodPresenter(view, get())
    }
}

val calendarModule = module {
    single { CalendarRepository() }
    factory { (view: CalendarContract.View) ->
        CalendarPresenter(view, get())
    }
}

val splashModule = module {
    single { SplashRepository() }
    factory { (view: SplashContract.View) ->
        SplashPresenter(view, get())
    }
}

val settingsModule = module {
    single { SettingsRepository() }
    factory { (view: SettingsContract.View) ->
        SettingsPresenter(view, get())
    }
}

val generatePdfModule = module {
    single { GeneratePdfRepository() }
    factory { (view: GeneratePdfContract.View) ->
        GeneratePdfPresenter(view, get())
    }
}