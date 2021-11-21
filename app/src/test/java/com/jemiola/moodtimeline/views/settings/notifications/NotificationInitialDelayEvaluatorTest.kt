package com.jemiola.moodtimeline.views.settings.notifications

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalTime

class NotificationInitialDelayEvaluatorTest {

    @Test
    fun `returns 90 minutes for time now 8 am and notification time 9 30 am`() {
        val evaluator = NotificationInitialDelayEvaluator()
        val timeNow = LocalTime.of(8, 0)
        val notificationTime = LocalTime.of(9, 30)
        val result = evaluator.getInitialDelayInMinutes(notificationTime, timeNow)
        assertEquals(90, result)
    }

    @Test
    fun `returns 180 minutes for time now 8 am and notification time 11 am`() {
        val evaluator = NotificationInitialDelayEvaluator()
        val timeNow = LocalTime.of(8, 0)
        val notificationTime = LocalTime.of(11, 0)
        val result = evaluator.getInitialDelayInMinutes(notificationTime, timeNow)
        assertEquals(180, result)
    }

    @Test
    fun `returns 1208 minutes for time now 3 23 am and notification time 11 41 pm`() {
        val evaluator = NotificationInitialDelayEvaluator()
        val timeNow = LocalTime.of(3, 23)
        val notificationTime = LocalTime.of(23, 31)
        val result = evaluator.getInitialDelayInMinutes(notificationTime, timeNow)
        assertEquals(1208, result)
    }

    @Test
    fun `returns 1380 minutes for time now 9 am and notification time 8 am`() {
        val evaluator = NotificationInitialDelayEvaluator()
        val timeNow = LocalTime.of(9, 0)
        val notificationTime = LocalTime.of(8, 0)
        val result = evaluator.getInitialDelayInMinutes(notificationTime, timeNow)
        assertEquals(1380, result)
    }
}