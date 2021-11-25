package com.jemiola.moodtimeline.views.settings.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import org.threeten.bp.LocalTime
import java.util.concurrent.TimeUnit

private const val NOTIFICATION_TAG = "notification_worker_tag"
private const val NOTIFICATION_NAME = "notification_work_name"

class NotificationWorkManager(private val context: Context) {
    private val workManager by lazy { WorkManager.getInstance(context) }

    fun runNotificationRequest(notificationTime: LocalTime, timeNow: LocalTime) {
        val initialDelay =
            NotificationInitialDelayEvaluator().getInitialDelayInMinutes(notificationTime, timeNow)
        val notificationWorker = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MINUTES)
            .addTag(NOTIFICATION_TAG)
            .build()
        workManager.enqueueUniquePeriodicWork(
            NOTIFICATION_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorker
        )
    }

    fun cancelAllWork() {
        workManager.cancelAllWork()
    }
}