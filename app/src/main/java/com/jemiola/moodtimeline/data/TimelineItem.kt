package com.jemiola.moodtimeline.data

import org.threeten.bp.LocalDate
import java.io.Serializable

data class TimelineItem(
    val date: LocalDate,
    val note: String,
    val mood: CircleMood,
    val state: CircleState = CircleState.DEFAULT
) : Serializable