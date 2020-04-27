package com.jemiola.moodtimeline.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jemiola.moodtimeline.R

class CalendarDayView : FrameLayout {

    val dayTextView: ComfortaBoldTextView
    var day: Int = 0
        set(value) {
            dayTextView.text = value.toString()
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
        LayoutInflater.from(context).inflate(R.layout.view_calendar_day, this)
        dayTextView = findViewById(R.id.dayNumberTextView)
    }

}