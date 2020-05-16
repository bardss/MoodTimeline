package com.jemiola.moodtimeline.model.data.local

import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.model.data.databaseobjects.MoodDO

enum class CircleMoodBO(
    val colorId: Int,
    val backgroundId: Int,
    val moodDrawable: Int
) {
    VERY_GOOD(
        R.color.colorMoodVeryGood,
        R.drawable.mood_circle_very_good,
        R.drawable.ic_very_good
    ),
    GOOD(
        R.color.colorMoodGood,
        R.drawable.mood_circle_good,
        R.drawable.ic_good
    ),
    MEDIOCRE(
        R.color.colorMoodMediocre,
        R.drawable.mood_circle_medicore,
        R.drawable.ic_mediocre
    ),
    BAD(
        R.color.colorMoodBad,
        R.drawable.mood_circle_bad,
        R.drawable.ic_bad
    ),
    VERY_BAD(
        R.color.colorMoodVeryBad,
        R.drawable.mood_circle_very_bad,
        R.drawable.ic_very_bad
    ),
    NONE(
        R.color.colorMoodNone,
        R.drawable.mood_circle_none,
        R.drawable.ic_mood_none
    );

    companion object {
        fun from(mood: MoodDO): CircleMoodBO {
            return when (mood) {
                MoodDO.VERY_GOOD -> VERY_GOOD
                MoodDO.GOOD -> GOOD
                MoodDO.MEDIOCRE -> MEDIOCRE
                MoodDO.BAD -> BAD
                MoodDO.VERY_BAD -> VERY_BAD
                MoodDO.NONE -> NONE
            }
        }
    }
}