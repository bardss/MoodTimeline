package com.jemiola.moodtimeline.views.settings.notifications

import com.jemiola.moodtimeline.base.BasePresenter
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

private const val TIME_FORMAT = ""

class NotificationsPresenter(
    val view: NotificationsContract.View,
    override val repository: NotificationsRepository
) : BasePresenter(repository), NotificationsContract.Presenter