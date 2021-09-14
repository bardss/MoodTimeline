package com.jemiola.moodtimeline.utils

import android.content.Context
import android.os.Build
import java.util.*

object LocaleUtil {

    fun getSystemLocale(context: Context): Locale {
        val configuration = context.resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales[0]
        } else {
            configuration.locale
        }
    }
}