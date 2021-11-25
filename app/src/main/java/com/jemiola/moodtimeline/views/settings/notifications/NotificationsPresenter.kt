package com.jemiola.moodtimeline.views.settings.notifications

import com.jemiola.moodtimeline.base.BasePresenter

class NotificationsPresenter(
    val view: NotificationsContract.View,
    override val repository: NotificationsRepository
) : BasePresenter(repository), NotificationsContract.Presenter {

    fun saveNotificationTime(notificationTime: String) {
        repository.saveNotificationTime(notificationTime)
    }
}