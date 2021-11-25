package com.jemiola.moodtimeline.model.data.local

import com.jemiola.moodtimeline.model.data.databaseobjects.TimelineMoodDOv2
import org.threeten.bp.LocalDate
import java.io.Serializable

data class TimelineMoodBOv2(
    val id: Int? = null,
    val date: LocalDate,
    val note: String,
    val circleMood: CircleMoodBO,
    var circleState: CircleStateBO = CircleStateBO.DEFAULT,
    val picturesPaths: List<String>
) : Serializable

fun TimelineMoodDOv2.convertTimelineMoodsDOtoBO() =
    TimelineMoodBOv2(
        id = id,
        date = date,
        note = note,
        circleMood = CircleMoodBO.from(mood),
        picturesPaths = picturesPaths
    )

fun List<TimelineMoodDOv2>.convertTimelineMoodsDOtoBO() = map { it.convertTimelineMoodsDOtoBO() }
