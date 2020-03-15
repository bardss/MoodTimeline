package com.jemiola.moodtimeline.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.data.CircleMood
import com.jemiola.moodtimeline.data.CircleMood.NONE
import com.jemiola.moodtimeline.data.CircleState
import com.jemiola.moodtimeline.data.CircleState.*
import com.jemiola.moodtimeline.data.MoodCircleState
import com.jemiola.moodtimeline.utils.ResUtil
import kotlin.properties.Delegates

class MoodCircle : FrameLayout {

    var state: MoodCircleState by Delegates.observable(
        MoodCircleState(NONE, DEFAULT)
    ) { _, _, state ->
        changeMoodCircleState(state)
    }
    private val stateImageView: ImageView
    private val moodImageView: ImageView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.view_mood_circle, this)
        moodImageView = findViewById(R.id.moodImageView)
        stateImageView = findViewById(R.id.stateImageView)
    }

    private fun changeMoodCircleState(moodCircleState: MoodCircleState) {
        changeState(moodCircleState.circleState)
        changeBackground(moodCircleState.mood)
    }

    private fun changeBackground(mood: CircleMood) {
        moodImageView.setImageDrawable(mood.background)
    }

    private fun changeState(state: CircleState) {
        when (state) {
            EDIT -> R.drawable.ic_pen
            ADD -> R.drawable.ic_plus
            DEFAULT -> null
        }?.also { stateDrawableId ->
            val stateDrawable = ResUtil.getDrawable(stateDrawableId)
            stateImageView.setImageDrawable(stateDrawable)
        }
    }
}