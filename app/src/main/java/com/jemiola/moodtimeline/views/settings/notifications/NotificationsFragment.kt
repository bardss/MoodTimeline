package com.jemiola.moodtimeline.views.settings.notifications

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jemiola.moodtimeline.base.BaseFragmentMVP
import com.jemiola.moodtimeline.databinding.FragmentNotificationsBinding
import com.jemiola.moodtimeline.utils.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.threeten.bp.LocalTime
import java.util.*

private const val FAST_ANIM_DURATION = 200
private const val SUCCESS_DIALOG_DURATION = 3500

class NotificationsFragment : BaseFragmentMVP(), NotificationsContract.View {

    override val presenter: NotificationsPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var notificationWorkManager: NotificationWorkManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentNotificationsBinding.inflate(inflater, container, false)
            notificationWorkManager = NotificationWorkManager(binding.root.context)
            setupButtons()
            setupTimePicker()
        }
        return binding.root
    }

    override fun onStart() {
        hideBottomMenu()
        super.onStart()
    }

    override fun onStop() {
        showBottomMenu()
        super.onStop()
    }

    private fun setupButtons() {
        binding.notificationLayout.turnOnNotificationView.setOnClickListener {
            onTurnOnNotificationClick()
        }
        binding.notificationLayout.turnOffNotificationView.setOnClickListener {
            turnOffNotifications()
            popFragment()
        }
        binding.notificationLayout.closeTextView.setOnClickListener {
            popFragment()
        }
    }

    private fun turnOffNotifications() {
        notificationWorkManager.cancelAllWork()
        presenter.saveNotificationTime("")
    }

    private fun onTurnOnNotificationClick() {
        val hour = binding.notificationLayout.timePicker.getHourCompat()
        val minute = binding.notificationLayout.timePicker.getMinuteCompat()
        val notificationTime = LocalTime.of(hour, minute)
        val timeNow = LocalTime.now()
        notificationWorkManager.cancelAllWork()
        notificationWorkManager.runNotificationRequest(notificationTime, timeNow)
        val timeText = getTimeFormattedToLocale(hour, minute)
        presenter.saveNotificationTime(timeText)
        showNotificationSuccessDialog(timeText)
    }

    private fun getTimeFormattedToLocale(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        return DateFormat.getTimeFormat(context).format(calendar.time)
    }

    private fun showNotificationSuccessDialog(time: String) {
        AnimUtils.fadeOut(FAST_ANIM_DURATION, {
            AnimUtils.fadeIn(FAST_ANIM_DURATION, {
                binding.successDialogLayout.notificationAnimationView.playAnimation()
                binding.successDialogLayout.reminderTimeTextView.text = time
                DelayedTask(this).run(SUCCESS_DIALOG_DURATION) {
                    AnimUtils.fadeOut(
                        FAST_ANIM_DURATION, { popFragment() },
                        binding.successDialogLayout.notificationDialogContentLayout
                    )
                }
            }, binding.successDialogLayout.notificationDialogContentLayout)
        }, binding.notificationLayout.notificationContainerLayout)
    }

    private fun setupTimePicker() {
        val locale = LocaleUtil.getSystemLocale(binding.notificationLayout.timePicker.context)
        val isAmPmTime = TimeUtil.shouldShowAmPmForLocale(locale)
        binding.notificationLayout.timePicker.setIs24HourView(!isAmPmTime)
    }
}