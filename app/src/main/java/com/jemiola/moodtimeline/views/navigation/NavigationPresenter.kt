package com.jemiola.moodtimeline.views.navigation

import com.jemiola.moodtimeline.base.BasePresenterMVP

class NavigationPresenter(
    private val view: NavigationContract.View,
    override val repository: NavigationRepository
) : BasePresenterMVP(repository), NavigationContract.Presenter