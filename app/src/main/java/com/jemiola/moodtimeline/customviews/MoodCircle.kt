package com.jemiola.moodtimeline.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO.NONE
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO.*
import com.jemiola.moodtimeline.utils.ResUtil
import kotlin.properties.Delegates

class MoodCircle : FrameLayout {

    var state: CircleStateBO by Delegates.observable(DEFAULT) { _, _, state ->
        changeState(state)
    }
    var mood: CircleMoodBO by Delegates.observable(NONE) { _, _, mood ->
        changeBackground(mood)
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
        setupStatePadding()
    }

    private fun setupStatePadding() {
        viewTreeObserver.addOnGlobalLayoutListener {
            stateImageView.layoutParams = stateImageView.layoutParams.apply {
                width = (moodImageView.width / 3) * 2
                height = (moodImageView.height / 3) * 2
            }
        }
    }

    private fun changeBackground(mood: CircleMoodBO) {
        moodImageView.setImageDrawable(
            ResUtil.getDrawable(mood.backgroundId)
        )
    }

    private fun changeState(state: CircleStateBO) {
        when (state) {
            EDIT -> ResUtil.getDrawable(R.drawable.ic_edit)
            ADD -> ResUtil.getDrawable(R.drawable.ic_plus)
            DEFAULT -> null
        }.also { stateDrawable ->
            stateImageView.setImageDrawable(stateDrawable)
        }
    }
}