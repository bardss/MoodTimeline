package com.jemiola.moodtimeline.model.data.local

import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.model.data.databaseobjects.MoodDO

enum class CircleMoodBO(
    val colorId: Int,
    val backgroundId: Int,
    val moodDrawable: Int,
    val moodDrawablePdf: Int
) {
    VERY_GOOD(
        R.color.colorMoodVeryGood,
        R.drawable.mood_circle_very_good,
        R.drawable.ic_very_good,
        R.drawable.ic_very_good_pdf
    ),
    GOOD(
        R.color.colorMoodGood,
        R.drawable.mood_circle_good,
        R.drawable.ic_good,
        R.drawable.ic_good_pdf
    ),
    MEDIOCRE(
        R.color.colorMoodMediocre,
        R.drawable.mood_circle_medicore,
        R.drawable.ic_mediocre,
        R.drawable.ic_mediocre_pdf
    ),
    BAD(
        R.color.colorMoodBad,
        R.drawable.mood_circle_bad,
        R.drawable.ic_bad,
        R.drawable.ic_bad_pdf
    ),
    VERY_BAD(
        R.color.colorMoodVeryBad,
        R.drawable.mood_circle_very_bad,
        R.drawable.ic_very_bad,
        R.drawable.ic_very_bad_pdf
    ),
    NONE(
        R.color.colorMoodNone,
        R.drawable.mood_circle_none,
        R.drawable.ic_mood_none,
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