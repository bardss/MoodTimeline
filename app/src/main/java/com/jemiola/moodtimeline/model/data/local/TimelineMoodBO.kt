package com.jemiola.moodtimeline.model.data.local

import org.threeten.bp.LocalDate
import java.io.Serializable

data class TimelineMoodBO(
    val date: LocalDate,
    val note: String,
    val circleMood: CircleMoodBO,
    val circleState: CircleStateBO = CircleStateBO.DEFAULT
) : Serializable