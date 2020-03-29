package com.jemiola.moodtimeline.views.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseActivity
import com.jemiola.moodtimeline.views.navigation.NavigationActivity
import com.jemiola.moodtimeline.views.timeline.TimelineFragment
import org.koin.core.inject

const val splashDelayMilis = 2000L

class SplashActivity : BaseActivity() {

    override val presenter: SplashPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
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
