package com.jemiola.moodtimeline.base

import com.jemiola.moodtimeline.views.editmood.EditMoodContract
import com.jemiola.moodtimeline.views.editmood.EditMoodPresenter
import com.jemiola.moodtimeline.views.editmood.EditMoodRepository
import com.jemiola.moodtimeline.views.mooddetails.MoodDetailsContract
import com.jemiola.moodtimeline.views.mooddetails.MoodDetailsPresenter
import com.jemiola.moodtimeline.views.mooddetails.MoodDetailsRepository
import com.jemiola.moodtimeline.views.moods.MoodsContract
import com.jemiola.moodtimeline.views.moods.MoodsPresenter
import com.jemiola.moodtimeline.views.moods.MoodsRepository
import com.jemiola.moodtimeline.views.moods.calendar.CalendarContract
import com.jemiola.moodtimeline.views.moods.calendar.CalendarPresenter
import com.jemiola.moodtimeline.views.moods.calendar.CalendarRepository
import com.jemiola.moodtimeline.views.moods.list.MoodsAdapterPresenter
import com.jemiola.moodtimeline.views.moods.search.SearchContract
import com.jemiola.moodtimeline.views.moods.search.SearchPresenter
import com.jemiola.moodtimeline.views.moods.search.SearchRepository
import com.jemiola.moodtimeline.views.moods.timeline.TimelineContract
import com.jemiola.moodtimeline.views.moods.timeline.TimelinePresenter
import com.jemiola.moodtimeline.views.moods.timeline.TimelineRepository
import com.jemiola.moodtimeline.views.navigation.NavigationContract
import com.jemiola.moodtimeline.views.navigation.NavigationPresenter
import com.jemiola.moodtimeline.views.navigation.NavigationRepository
import com.jemiola.moodtimeline.views.settings.SettingsContract
import com.jemiola.moodtimeline.views.settings.SettingsPresenter
import com.jemiola.moodtimeline.views.settings.SettingsRepository
import com.jemiola.moodtimeline.views.settings.generatepdf.GeneratePdfContract
import com.jemiola.moodtimeline.views.settings.generatepdf.GeneratePdfPresenter
import com.jemiola.moodtimeline.views.settings.generatepdf.GeneratePdfRepository
import com.jemiola.moodtimeline.views.settings.notifications.NotificationsContract
import com.jemiola.moodtimeline.views.settings.notifications.NotificationsPresenter
import com.jemiola.moodtimeline.views.settings.notifications.NotificationsRepository
import com.jemiola.moodtimeline.views.splash.SplashContract
import com.jemiola.moodtimeline.views.splash.SplashPresenter
import com.jemiola.moodtimeline.views.splash.SplashRepository
import org.koin.dsl.module

val navigationModule = module {
    single { NavigationRepository() }
    factory { (view: NavigationContract.View) ->
        NavigationPresenter(view, get())
    }
}

val timelineModule = module {
    single { TimelineRepository() }
    single { MoodsAdapterPresenter() }
    factory { (view: TimelineContract.View) ->
        TimelinePresenter(view, get())
    }
}

val editTimelineItemModule = module {
    single { EditMoodRepository() }
    factory { (view: EditMoodContract.View) ->
        EditMoodPresenter(view, get())
    }
}

val detailsTimelineItemModule = module {
    single { MoodDetailsRepository() }
    factory { (view: MoodDetailsContract.View) ->
        MoodDetailsPresenter(view, get())
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

val notificationModule = module {
    single { NotificationsRepository() }
    factory { (view: NotificationsContract.View) ->
        NotificationsPresenter(view, get())
    }
}

val moodsModule = module {
    single { MoodsRepository() }
    factory { (view: MoodsContract.View) ->
        MoodsPresenter(view, get())
    }
}

val searchModule = module {
    single { SearchRepository() }
    factory { (view: SearchContract.View) ->
        SearchPresenter(view, get())
    }
}