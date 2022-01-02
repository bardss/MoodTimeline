package com.jemiola.moodtimeline.views.settings

import com.jemiola.moodtimeline.base.BaseRepositoryMVP
import com.jemiola.moodtimeline.model.localdatabase.DatabaseKeys
import com.jemiola.moodtimeline.model.localdatabase.LocalNoSQLDatabase

class SettingsRepository : BaseRepositoryMVP() {

    private val databaseNoSQL = LocalNoSQLDatabase.getInstance()

    fun saveAppTheme(themeKey: Int) {
        val editInstance = databaseNoSQL?.edit()
        editInstance?.putInt(DatabaseKeys.APP_THEME, themeKey)
        editInstance?.apply()
    }

    fun getAppTheme(): Int? {
        val errorValue = 500
        val theme = databaseNoSQL?.getInt(DatabaseKeys.APP_THEME, errorValue)
        return if (theme != 500) theme else null
    }

    fun getNotificationTime(): String {
        val notificationTime = databaseNoSQL?.getString(DatabaseKeys.NOTIFICATION_TIME, "")
        return notificationTime ?: ""
    }
}