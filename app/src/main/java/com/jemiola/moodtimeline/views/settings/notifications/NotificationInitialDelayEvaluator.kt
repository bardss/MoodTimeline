package com.jemiola.moodtimeline.views.settings.notifications

import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

class NotificationInitialDelayEvaluator {

    fun getInitialDelayInMinutes(notificationTime: LocalTime, timeNow: LocalTime): Long {
        val isNotificationOnTheSameDayLater = notificationTime.isAfter(timeNow)
        return if (isNotificationOnTheSameDayLater) {
            Duration.between(timeNow, notificationTime).toMinutes()
        } else {
            val midnight = LocalTime.MIDNIGHT
            val fromNowToMidnightMinutes = Duration.between(timeNow, midnight).toMinutes()
            val fromMidnightToNotificationMinutes =
                Duration.between(midnight, notificationTime).toMinutes()
            fromNowToMidnightMinutes + fromMidnightToNotificationMinutes
        }
    }
}