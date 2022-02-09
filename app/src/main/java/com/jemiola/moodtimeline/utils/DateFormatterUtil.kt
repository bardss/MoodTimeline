package com.jemiola.moodtimeline.utils

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class DateFormatterUtil {

    fun getDateFormat(locale: Locale): DateTimeFormatter = when (locale) {
        Locale.US, Locale.CANADA -> DateTimeFormatter.ofPattern("MM-dd-yyyy")
        else -> DateTimeFormatter.ofPattern("dd.MM.yyyy")
    }

    fun getFormattedDate(locale: Locale, date: LocalDate): String = getDateFormat(locale).format(date)

    fun getDateFromFormattedString(locale: Locale, date: String): LocalDate =
        LocalDate.parse(date, getDateFormat(locale))

    fun createDateTextFrom(locale: Locale, dayOfMonth: Int, monthOfYear: Int, year: Int): String {
        val selectedDate = LocalDate.of(year, monthOfYear, dayOfMonth)
        val formatter = getDateFormat(locale)
        return selectedDate.format(formatter)
    }
}