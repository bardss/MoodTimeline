package com.jemiola.moodtimeline.views.settings

import com.jemiola.moodtimeline.base.BasePresenter

class SettingsPresenter(
    val view: SettingsContract.View,
    override val repository: SettingsRepository
) : BasePresenter(repository), SettingsContract.Presenter {

    override fun saveAppTheme(theme: Int) {
        repository.saveAppTheme(theme)
    }

    override fun setupCurrentThemeText() {
        val appTheme = repository.getAppTheme()
        view.setCurrentThemeText(appTheme)
    }

    fun setupNotificationBar() {
        val notificationTime = repository.getNotificationTime()
        if (notificationTime.isNotEmpty()) {
            view.setNotificationOn(notificationTime)
        } else {
            view.setNotificationOff()
        }
    }
}