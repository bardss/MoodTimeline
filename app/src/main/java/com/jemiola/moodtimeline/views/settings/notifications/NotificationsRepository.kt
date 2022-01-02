package com.jemiola.moodtimeline.views.settings.notifications

import androidx.room.Room
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.base.BaseRepositoryMVP
import com.jemiola.moodtimeline.base.DatabasesNames
import com.jemiola.moodtimeline.model.localdatabase.DatabaseKeys
import com.jemiola.moodtimeline.model.localdatabase.LocalNoSQLDatabase
import com.jemiola.moodtimeline.model.localdatabase.LocalSQLDatabase
import org.threeten.bp.LocalDate

class NotificationsRepository : BaseRepositoryMVP() {

    private val databaseNoSQL = LocalNoSQLDatabase.getInstance()

    private val database = Room.databaseBuilder(
        BaseApplication.context,
        LocalSQLDatabase::class.java, DatabasesNames.moodsDatabase
    ).build()

    fun isAnyMoodAddedOnDate(date: LocalDate): Boolean {
        return database.timelineMoodDaoV2().getMoodsFromTo(date, date).isNotEmpty()
    }

    fun saveNotificationTime(notificationTime: String) {
        val editInstance = databaseNoSQL?.edit()
        editInstance?.putString(DatabaseKeys.NOTIFICATION_TIME, notificationTime)
        editInstance?.apply()
    }
}