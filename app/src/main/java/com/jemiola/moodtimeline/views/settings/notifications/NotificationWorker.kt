package com.jemiola.moodtimeline.views.settings.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.views.navigation.NavigationActivity
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.LocalDate
import java.util.concurrent.atomic.AtomicInteger

private const val CHANNEL_ID = "notification_channel_id"

class NotificationWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private var notificationId = AtomicInteger()
    private val notificationsRepository: NotificationsRepository by inject()

    override suspend fun doWork(): Result {
        if (!notificationsRepository.isAnyMoodAddedOnDate(LocalDate.now())) {
            notifyAddMood()
        }
        return Result.success()
    }

    private fun notifyAddMood() {
        val openAppIntent = Intent(context, NavigationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            context, 0, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationTitle = context.resources.getString(R.string.add_note_notification_title)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(notificationTitle)
            .setSmallIcon(R.mipmap.app_logo_round)
            .setContentIntent(openAppPendingIntent)
            .setPriority(Notification.PRIORITY_HIGH)
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notification, CHANNEL_ID)
        }
        with(NotificationManagerCompat.from(context)) {
            notify(generateNextNotificationId(), notification.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(notificationBuilder: NotificationCompat.Builder, channelId: String) {
        val appName = context.resources.getString(R.string.app_name)
        val notificationDescription = context.resources.getString(R.string.app_notification)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL)
        val channel = NotificationChannel(
            channelId,
            appName,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = notificationDescription
        notificationManager.createNotificationChannel(channel)
    }

    private fun generateNextNotificationId() = notificationId.getAndIncrement()
}