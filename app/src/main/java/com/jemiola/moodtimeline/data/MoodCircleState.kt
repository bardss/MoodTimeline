package com.jemiola.moodtimeline.data

data class MoodCircleState(
    val mood: CircleMood,
    val circleState: CircleState = CircleState.DEFAULT
)