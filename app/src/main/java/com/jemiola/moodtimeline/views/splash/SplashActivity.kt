package com.jemiola.moodtimeline.views.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseActivity
import com.jemiola.moodtimeline.views.navigation.NavigationActivity
import com.jemiola.moodtimeline.views.timeline.TimelineFragment
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

const val splashDelayMilis = 2000L

class SplashActivity : BaseActivity(), SplashContract.View {

    override val presenter: SplashPresenter by inject { parametersOf(this)  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        presenter.executeDatabaseMigrations()
    }

    override fun onMigrationsEnd() {
        openNextActivityWithDelay()
    }

    private fun openNextActivityWithDelay() {
        Handler().postDelayed({
            openTimelineActivity()
            finish()
        }, splashDelayMilis)
    }

    private fun openTimelineActivity() {
        startActivity(Intent(this, NavigationActivity::class.java))
    }
}
