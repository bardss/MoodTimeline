package com.jemiola.moodtimeline.views.navigation

import com.jemiola.moodtimeline.base.BasePresenter

class NavigationPresenter(
    public val view: NavigationContract.View,
    override val repository: NavigationRepository
) : BasePresenter(repository), NavigationContract.Presenter