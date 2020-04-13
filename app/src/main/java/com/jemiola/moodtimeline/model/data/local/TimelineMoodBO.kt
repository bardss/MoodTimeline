package com.jemiola.moodtimeline.model.data.local

import org.threeten.bp.LocalDate
import java.io.Serializable

data class TimelineMoodBO(
    val id: Int? = null,
    val date: LocalDate,
    val note: String,
    val circleMood: CircleMoodBO,
    var circleState: CircleStateBO = CircleStateBO.DEFAULT,
    val picturePath: String
) : Serializable