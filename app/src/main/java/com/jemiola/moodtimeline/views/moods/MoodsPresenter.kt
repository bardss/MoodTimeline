package com.jemiola.moodtimeline.views.moods

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.views.moods.timeline.TimelineContract
import com.jemiola.moodtimeline.views.moods.timeline.TimelineRepository

class MoodsPresenter(
    private val view: MoodsContract.View,
    override val repository: MoodsRepository
) : BasePresenter(repository), MoodsContract.Presenter