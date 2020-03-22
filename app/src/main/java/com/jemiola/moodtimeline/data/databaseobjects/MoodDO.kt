package com.jemiola.moodtimeline.data.databaseobjects

import com.jemiola.moodtimeline.data.local.CircleMoodBO

enum class MoodDO {
    VERY_GOOD,
    GOOD,
    MEDIOCRE,
    BAD,
    VERY_BAD,
    NONE;

    companion object {
        fun from(mood: CircleMoodBO) : MoodDO {
            return when (mood) {
                CircleMoodBO.VERY_GOOD -> VERY_GOOD
                CircleMoodBO.GOOD -> GOOD
                CircleMoodBO.MEDIOCRE -> MEDIOCRE
                CircleMoodBO.BAD -> BAD
                CircleMoodBO.VERY_BAD -> VERY_BAD
                CircleMoodBO.NONE -> NONE
            }
        }
    }
}