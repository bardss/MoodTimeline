package com.jemiola.moodtimeline.views.moods

import com.jemiola.moodtimeline.base.BasePresenterMVP

class MainMoodsPresenter(
    private val view: MainMoodsContract.View,
    override val repository: MainMoodsRepository
) : BasePresenterMVP(repository), MainMoodsContract.Presenter