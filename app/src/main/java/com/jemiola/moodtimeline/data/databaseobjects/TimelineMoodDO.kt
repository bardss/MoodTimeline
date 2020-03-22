package com.jemiola.moodtimeline.data.databaseobjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate

@Entity
data class TimelineMoodDO(
    @PrimaryKey val id: Int,
    val note: String,
    val date: LocalDate,
    val mood: MoodDO
)