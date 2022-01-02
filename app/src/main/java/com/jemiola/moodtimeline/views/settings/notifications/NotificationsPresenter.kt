package com.jemiola.moodtimeline.views.settings.notifications

import com.jemiola.moodtimeline.base.BasePresenterMVP

class NotificationsPresenter(
    val view: NotificationsContract.View,
    override val repository: NotificationsRepository
) : BasePresenterMVP(repository), NotificationsContract.Presenter {

    fun saveNotificationTime(notificationTime: String) {
        repository.saveNotificationTime(notificationTime)
    }
}