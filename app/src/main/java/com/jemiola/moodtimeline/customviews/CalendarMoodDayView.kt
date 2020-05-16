package com.jemiola.moodtimeline.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO

class CalendarMoodDayView : FrameLayout {

    private val dayMoodCircle: MoodCircle
    var day: Int = 0
        set(value) {
            dayMoodCircle.day = value.toString()
            field = value
        }
    var mood: CircleMoodBO = CircleMoodBO.NONE
        set(value) {
            dayMoodCircle.mood = value
            dayMoodCircle.state = CircleStateBO.DEFAULT
            field = value
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.view_calendar_mood_day, this)
        dayMoodCircle = findViewById(R.id.dayMoodCircle)
    }

}