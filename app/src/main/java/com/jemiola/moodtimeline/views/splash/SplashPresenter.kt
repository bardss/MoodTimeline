package com.jemiola.moodtimeline.views.splash

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.views.timeline.TimelineContract
import com.jemiola.moodtimeline.views.timeline.TimelineRepository

class SplashPresenter(
    val view: SplashContract.View,
    override val repository: SplashRepository
) : BasePresenter(repository), SplashContract.Presenter {

    override fun executeDatabaseMigrations() {
        val callback = createRepositoryCallback<Unit>(
            { view.onMigrationsEnd() },
            { view.onMigrationsEnd() }
        )
        repository.executeMigrations(callback)
    }
}