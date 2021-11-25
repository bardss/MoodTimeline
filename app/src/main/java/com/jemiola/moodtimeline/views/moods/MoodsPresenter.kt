package com.jemiola.moodtimeline.views.moods

import com.jemiola.moodtimeline.base.BasePresenter

class MoodsPresenter(
    private val view: MoodsContract.View,
    override val repository: MoodsRepository
) : BasePresenter(repository), MoodsContract.Presenter