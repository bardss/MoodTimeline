package com.jemiola.moodtimeline.views.settings.notifications

import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

class NotificationInitialDelayEvaluator {

    fun getInitialDelayInMinutes(notificationTime: LocalTime, timeNow: LocalTime): Long {
        val isNotificationOnTheSameDayLater = notificationTime.isAfter(timeNow)
        return if (isNotificationOnTheSameDayLater) {
            Duration.between(timeNow, notificationTime).toMinutes()
        } else {
            val randomDate = LocalDate.MIN
            val notificationDateTime = LocalDateTime.of(randomDate.plusDays(1), notificationTime)
            val timeNowDateTime = LocalDateTime.of(randomDate, timeNow)
            Duration.between(timeNowDateTime, notificationDateTime).toMinutes()
        }
    }
}