package com.jemiola.moodtimeline.utils

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class DateFormatterUtil {

    private fun getDateFormat(locale: Locale) = when (locale) {
        Locale.US, Locale.CANADA -> DateTimeFormatter.ofPattern("MM.dd.yyyy")
        else -> DateTimeFormatter.ofPattern("dd.MM.yyyy")
    }

    fun getFormattedDate(locale: Locale, date: LocalDate) = getDateFormat(locale).format(date)

    fun getDateFromFormattedString(locale: Locale, date: String) =
        LocalDate.parse(date, getDateFormat(locale))
}