package com.jemiola.moodtimeline.base

import com.jemiola.moodtimeline.repositorymvvm.MoodsRepository
import com.jemiola.moodtimeline.views.editmood.EditMoodContract
import com.jemiola.moodtimeline.views.editmood.EditMoodPresenter
import com.jemiola.moodtimeline.views.editmood.EditMoodRepository
import com.jemiola.moodtimeline.views.mooddetails.MoodDetailsContract
import com.jemiola.moodtimeline.views.mooddetails.MoodDetailsPresenter
import com.jemiola.moodtimeline.views.mooddetails.MoodDetailsRepository
import com.jemiola.moodtimeline.views.moods.MainMoodsContract
import com.jemiola.moodtimeline.views.moods.MainMoodsPresenter
import com.jemiola.moodtimeline.views.moods.MainMoodsRepository
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
import com.jemiola.moodtimeline.views.stats.StatsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mvpViewsModule = module {
    single { SplashRepository() }
    factory { (view: SplashContract.View) ->
        SplashPresenter(view, get())
    }

    single { NavigationRepository() }
    factory { (view: NavigationContract.View) ->
        NavigationPresenter(view, get())
    }

    single { TimelineRepository() }
    single { MoodsAdapterPresenter() }
    factory { (view: TimelineContract.View) ->
        TimelinePresenter(view, get())
    }

    single { EditMoodRepository() }
    factory { (view: EditMoodContract.View) ->
        EditMoodPresenter(view, get())
    }

    single { MoodDetailsRepository() }
    factory { (view: MoodDetailsContract.View) ->
        MoodDetailsPresenter(view, get())
    }

    single { CalendarRepository() }
    factory { (view: CalendarContract.View) ->
        CalendarPresenter(view, get())
    }

    single { SettingsRepository() }
    factory { (view: SettingsContract.View) ->
        SettingsPresenter(view, get())
    }

    single { GeneratePdfRepository() }
    factory { (view: GeneratePdfContract.View) ->
        GeneratePdfPresenter(view, get())
    }

    single { NotificationsRepository() }
    factory { (view: NotificationsContract.View) ->
        NotificationsPresenter(view, get())
    }

    single { MainMoodsRepository() }
    factory { (view: MainMoodsContract.View) ->
        MainMoodsPresenter(view, get())
    }

    single { SearchRepository() }
    factory { (view: SearchContract.View) ->
        SearchPresenter(view, get())
    }
}

val mvvmViewsModule = module {
    single {
        MoodsRepository()
    }

    viewModel { StatsViewModel(get()) }
}
