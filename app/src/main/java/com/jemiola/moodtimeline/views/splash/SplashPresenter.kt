package com.jemiola.moodtimeline.views.splash

import com.jemiola.moodtimeline.base.BasePresenter

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