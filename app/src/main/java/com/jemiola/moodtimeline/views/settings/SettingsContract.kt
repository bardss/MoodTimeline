package com.jemiola.moodtimeline.views.settings

interface SettingsContract {
    interface Presenter {
        fun saveAppTheme(theme: Int)
        fun setupCurrentThemeText()
    }

    interface View {
        fun setCurrentThemeText(appThemeFromDatabase: Int?)
        fun setNotificationOn(notificationTime: String)
        fun setNotificationOff()
    }
}