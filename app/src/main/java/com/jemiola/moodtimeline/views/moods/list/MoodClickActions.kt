package com.jemiola.moodtimeline.views.moods.list

import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2

interface MoodClickActions {
    fun openEditTimelineMoodActivity(mood: TimelineMoodBOv2, isAddingFirstMood: Boolean = false)
    fun openTimelineMoodDetails(mood: TimelineMoodBOv2)
}