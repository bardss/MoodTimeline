package com.jemiola.moodtimeline.views.moods

import com.jemiola.moodtimeline.base.BasePresenterMVP

class MoodsPresenter(
    private val view: MoodsContract.View,
    override val repository: MoodsRepository
) : BasePresenterMVP(repository), MoodsContract.Presenter