package com.jemiola.moodtimeline.views.settings

import com.jemiola.moodtimeline.base.BasePresenter

class SettingsPresenter(
    val view: SettingsContract.View,
    override val repository: SettingsRepository
) : BasePresenter(repository), SettingsContract.Presenter {
}