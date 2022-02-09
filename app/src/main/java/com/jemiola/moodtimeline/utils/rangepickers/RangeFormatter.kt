package com.jemiola.moodtimeline.utils.rangepickers

import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class RangeFormatter {

    fun getDefaultSearchDateFormatter() =
        DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(Locale.ENGLISH)
}