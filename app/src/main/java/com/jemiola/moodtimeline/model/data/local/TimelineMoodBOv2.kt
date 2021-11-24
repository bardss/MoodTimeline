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

fun List<TimelineMoodDOv2>.convertTimelineMoodDOtoBO() = map {
    TimelineMoodBOv2(
        id = it.id,
        date = it.date,
        note = it.note,
        circleMood = CircleMoodBO.from(it.mood),
        picturesPaths = it.picturesPaths
    )
}
