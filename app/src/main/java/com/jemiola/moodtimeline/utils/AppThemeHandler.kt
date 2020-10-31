package com.jemiola.moodtimeline.utils

import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate

class AppThemeHandler {

    fun getCurrentNightMode(resources: Resources): Int {
        return if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
            AppCompatDelegate.getDefaultNightMode()
        } else when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_NO
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
        }
    }
}