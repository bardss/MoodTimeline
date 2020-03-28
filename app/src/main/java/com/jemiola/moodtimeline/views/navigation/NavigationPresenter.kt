package com.jemiola.moodtimeline.views.navigation

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.views.timeline.TimelineContract
import com.jemiola.moodtimeline.views.timeline.TimelineRepository

class NavigationPresenter(
    private val view: NavigationContract.View,
    override val repository: NavigationRepository
) : BasePresenter(repository), NavigationContract.Presenter