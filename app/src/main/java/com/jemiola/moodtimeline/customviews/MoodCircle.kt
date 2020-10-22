package com.jemiola.moodtimeline.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
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
    var day: String by Delegates.observable("") { _, _, day ->
        dayTextView.text = day
        dayTextView.visibility = View.VISIBLE
    }
    private val stateImageView: ImageView
    private val circleImageView: ImageView
    private val moodImageView: ImageView
    private val dayTextView: ComfortaBoldTextView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.view_mood_circle, this)
        circleImageView = findViewById(R.id.circleImageView)
        stateImageView = findViewById(R.id.stateImageView)
        moodImageView = findViewById(R.id.moodImageView)
        dayTextView = findViewById(R.id.circleDayTextView)
        setupStatePadding()
    }

    private fun setupStatePadding() {
        viewTreeObserver.addOnGlobalLayoutListener {
            stateImageView.layoutParams = stateImageView.layoutParams.apply {
                width = (circleImageView.width / 3) * 2
                height = (circleImageView.height / 3) * 2
            }
        }
    }

    private fun changeBackground(mood: CircleMoodBO) {
        circleImageView.setImageDrawable(ResUtil.getDrawable(context.resources, mood.backgroundId))
        moodImageView.setImageDrawable(ResUtil.getDrawable(context.resources, mood.moodDrawable))
    }

    private fun changeState(state: CircleStateBO) {
        when (state) {
            EDIT -> ResUtil.getDrawable(context.resources, R.drawable.ic_edit)
            ADD -> ResUtil.getDrawable(context.resources, R.drawable.ic_plus)
            DEFAULT -> ResUtil.getDrawable(context.resources, R.drawable.ic_mood_none)
            CHOOSE_MOOD -> null
        }.also { stateDrawable ->
            stateImageView.setImageDrawable(stateDrawable)
            if (stateDrawable != null) moodImageView.visibility = View.GONE
            else moodImageView.visibility = View.VISIBLE
        }
    }
}