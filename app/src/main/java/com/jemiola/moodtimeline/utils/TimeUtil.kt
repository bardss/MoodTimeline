package com.jemiola.moodtimeline.utils

import java.util.*

object TimeUtil {

    fun shouldShowAmPmForLocale(locale: Locale): Boolean {
        return locale in listOf(Locale.CANADA, Locale.CANADA_FRENCH, Locale.US)
    }
}