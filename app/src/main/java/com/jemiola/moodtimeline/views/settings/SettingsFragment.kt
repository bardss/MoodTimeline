package com.jemiola.moodtimeline.views.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentSettingsBinding
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.AppThemeHandler
import com.jemiola.moodtimeline.utils.ResUtil
import com.jemiola.moodtimeline.views.settings.generatepdf.GeneratePdfFragment
import com.jemiola.moodtimeline.views.settings.notifications.NotificationsFragment
import com.yanzhenjie.permission.AndPermission
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

private const val ANIM_DURATION = 500

class SettingsFragment : BaseFragment(), SettingsContract.View {

    override val presenter: SettingsPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentSettingsBinding
    private val appThemeHandler = AppThemeHandler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentSettingsBinding.inflate(inflater, container, false)
            setupChangeThemeButton()
            setupNotificationButtom()
            setupGeneratePdfButton()
            presenter.setupCurrentThemeText()
        }
        return binding.root
    }

    override fun onBackPressed(): Boolean {
        return when {
            binding.changeThemeDialogLayout.changeThemeDialogContentLayout.visibility == View.VISIBLE -> {
                hideChangeThemeDialog()
                true
            }
            else -> super.onBackPressed()
        }
    }

    override fun setCurrentThemeText(appThemeFromDatabase: Int?) {
        val appTheme = appThemeFromDatabase ?: appThemeHandler.getCurrentNightMode(resources)
        val themeText = when (appTheme) {
            AppCompatDelegate.MODE_NIGHT_NO -> ResUtil.getString(resources, R.string.light)
            AppCompatDelegate.MODE_NIGHT_YES -> ResUtil.getString(resources, R.string.dark)
            else -> ""
        }
        val themeIcon = when (appTheme) {
            AppCompatDelegate.MODE_NIGHT_NO -> ResUtil.getDrawable(context, R.drawable.ic_sun)
            AppCompatDelegate.MODE_NIGHT_YES -> ResUtil.getDrawable(context, R.drawable.ic_moon)
            else -> null
        }
        binding.appThemeValueTextView.text = themeText
        binding.appThemeValueImageView.setImageDrawable(themeIcon)
    }

    private fun setupChangeThemeButton() {
        binding.themeButtonView.setOnClickListener {
            AnimUtils.fadeIn(
                ANIM_DURATION,
                binding.changeThemeDialogLayout.changeThemeDialogContentLayout
            )
            hideBottomMenu()
        }
        val themeDialogLayout = binding.changeThemeDialogLayout
        themeDialogLayout.changeThemeButtonView.setOnClickListener {
            changeTheme()
        }
        themeDialogLayout.backTextView.setOnClickListener {
            hideChangeThemeDialog()
        }
    }

    private fun changeTheme() {
        val themeToSet =
            if (appThemeHandler.getCurrentNightMode(resources) == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        AppCompatDelegate.setDefaultNightMode(themeToSet)
        presenter.saveAppTheme(themeToSet)
        restartApp()
    }

    private fun hideChangeThemeDialog() {
        val themeDialogLayout = binding.changeThemeDialogLayout.changeThemeDialogContentLayout
        AnimUtils.fadeOut(
            ANIM_DURATION, {
                themeDialogLayout.visibility = View.GONE
                showBottomMenu()
            },
            themeDialogLayout
        )
    }

    private fun setupGeneratePdfButton() {
        binding.exportMoodsPdfButtonView.setOnClickListener {
            pushFragment(GeneratePdfFragment())
        }
    }

    private fun setupNotificationButtom() {
        binding.notificationButtonView.setOnClickListener {
            pushFragment(NotificationsFragment())
        }
    }
}
