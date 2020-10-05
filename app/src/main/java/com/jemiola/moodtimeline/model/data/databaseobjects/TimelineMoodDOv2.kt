package com.jemiola.moodtimeline.model.data.databaseobjects

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate

@Entity
data class TimelineMoodDOv2(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0,
    val note: String,
    val date: LocalDate,
    val mood: MoodDO,
    val picturesPaths: List<String>
)