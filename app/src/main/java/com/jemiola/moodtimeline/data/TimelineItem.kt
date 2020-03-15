package com.jemiola.moodtimeline.data

import org.threeten.bp.LocalDate

data class TimelineItem(
    val date: LocalDate,
    val note: String,
    val mood: CircleMood
)