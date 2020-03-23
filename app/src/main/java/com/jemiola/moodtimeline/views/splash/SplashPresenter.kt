package com.jemiola.moodtimeline.views.splash

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.views.timeline.TimelineContract
import com.jemiola.moodtimeline.views.timeline.TimelineRepository

class SplashPresenter(
    override val repository: SplashRepository
) : BasePresenter(repository)