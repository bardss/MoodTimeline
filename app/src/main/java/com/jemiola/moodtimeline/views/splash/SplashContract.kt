package com.jemiola.moodtimeline.views.splash

import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import org.threeten.bp.LocalDate

interface SplashContract {
    interface Presenter {
        fun executeDatabaseMigrations()
    }

    interface View {
        fun onMigrationsEnd()
    }
}