package com.jemiola.moodtimeline.data.local

import android.graphics.drawable.Drawable
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.data.databaseobjects.MoodDO
import com.jemiola.moodtimeline.data.databaseobjects.MoodDO.*
import com.jemiola.moodtimeline.utils.ResUtil

enum class CircleMoodBO(
    val color: Int,
    val background: Drawable?
) {
    VERY_GOOD(
        ResUtil.getColor(R.color.colorMoodVeryGood),
        ResUtil.getDrawable(R.drawable.mood_circle_very_good)
    ),
    GOOD(
        ResUtil.getColor(R.color.colorMoodGood),
        ResUtil.getDrawable(R.drawable.mood_circle_good)
    ),
    MEDIOCRE(
        ResUtil.getColor(R.color.colorMoodMediocre),
        ResUtil.getDrawable(R.drawable.mood_circle_medicore)
    ),
    BAD(
        ResUtil.getColor(R.color.colorMoodBad),
        ResUtil.getDrawable(R.drawable.mood_circle_bad)
    ),
    VERY_BAD(
        ResUtil.getColor(R.color.colorMoodVeryBad),
        ResUtil.getDrawable(R.drawable.mood_circle_very_bad)
    ),
    NONE(
        ResUtil.getColor(R.color.colorMoodNone),
        ResUtil.getDrawable(R.drawable.mood_circle_none)
    );

    companion object {
        fun from(mood: MoodDO) : CircleMoodBO {
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