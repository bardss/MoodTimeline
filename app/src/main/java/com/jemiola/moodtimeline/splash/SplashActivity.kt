package com.jemiola.moodtimeline.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseActivity
import com.jemiola.moodtimeline.addtimelineitem.EditTimelineItemActivity
import com.jemiola.moodtimeline.timeline.TimelineActivity

const val splashDelayMilis = 2000L

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        openNextActivityWithDelay()
    }

    private fun openNextActivityWithDelay() {
        Handler().postDelayed({
            openTimelineActivity()
        }, splashDelayMilis)
    }

    private fun openTimelineActivity() {
        startActivity(Intent(this, TimelineActivity::class.java))
    }
}
