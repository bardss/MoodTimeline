package com.jemiola.moodtimeline.views.splash

interface SplashContract {
    interface Presenter {
        fun executeDatabaseMigrations()
    }

    interface View {
        fun onMigrationsEnd()
    }
}