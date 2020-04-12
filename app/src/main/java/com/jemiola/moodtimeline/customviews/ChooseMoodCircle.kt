package com.jemiola.moodtimeline.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.SizeUtils
import kotlin.properties.Delegates

const val ANIM_DURATION = 300
val MARGIN_BETWEEN_CIRCLES = SizeUtils.dp2px(10f)

class ChooseMoodCircle : FrameLayout {

    var selectedMood: CircleMoodBO by Delegates.observable(CircleMoodBO.NONE) { _, _, value ->
        onSelectedMoodChange.invoke(value)
    }

    private val veryGoodCircle: MoodCircle
    private val goodCircle: MoodCircle
    private val mediocreCircle: MoodCircle
    private val badCircle: MoodCircle
    private val veryBadCircle: MoodCircle
    private var expanded: Boolean = false

    private var onSelectedMoodChange: (CircleMoodBO) -> Unit = { }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.view_choose_mood_circle, this)
        veryGoodCircle = findViewById(R.id.veryGoodCircle)
        goodCircle = findViewById(R.id.goodCircle)
        mediocreCircle = findViewById(R.id.noneCircle)
        badCircle = findViewById(R.id.badCircle)
        veryBadCircle = findViewById(R.id.veryBadCircle)
        setupMoodColors()
        setupInitialState()
    }

    private fun setupInitialState() {
        mediocreCircle.state = CircleStateBO.EDIT
        mediocreCircle.setOnClickListener { expand() }
        veryGoodCircle.setOnClickListener { collapse(CircleMoodBO.VERY_GOOD) }
        goodCircle.setOnClickListener { collapse(CircleMoodBO.GOOD) }
        badCircle.setOnClickListener { collapse(CircleMoodBO.BAD) }
        veryBadCircle.setOnClickListener { collapse(CircleMoodBO.VERY_BAD) }
    }

    private fun setupMoodColors() {
        veryGoodCircle.mood = CircleMoodBO.VERY_GOOD
        goodCircle.mood = CircleMoodBO.GOOD
        mediocreCircle.mood = CircleMoodBO.NONE
        badCircle.mood = CircleMoodBO.BAD
        veryBadCircle.mood = CircleMoodBO.VERY_BAD
    }

    private fun expand() {
        if (!expanded) {
            mediocreCircle.state = CircleStateBO.DEFAULT
            mediocreCircle.mood = CircleMoodBO.MEDIOCRE
            mediocreCircle.setOnClickListener { collapse(CircleMoodBO.MEDIOCRE) }
            circleExpandAnimation()
            expanded = true
        }
    }

    private fun collapse(mood: CircleMoodBO) {
        if (expanded) {
            mediocreCircle.state = CircleStateBO.EDIT
            mediocreCircle.mood = mood
            selectedMood = mood
            mediocreCircle.setOnClickListener { expand() }
            circleCollapseAnimation()
            mediocreCircle.bringToFront()
            expanded = false
        }
    }

    private fun circleExpandAnimation() {
        val distance = mediocreCircle.width + MARGIN_BETWEEN_CIRCLES
        AnimUtils.animateMove(ANIM_DURATION, -(distance * 2), veryGoodCircle)
        AnimUtils.animateMove(ANIM_DURATION, -distance, goodCircle)
        AnimUtils.animateMove(ANIM_DURATION, distance, badCircle)
        AnimUtils.animateMove(ANIM_DURATION, distance * 2, veryBadCircle)
    }

    private fun circleCollapseAnimation() {
        AnimUtils.animateMove(ANIM_DURATION, 0, veryGoodCircle)
        AnimUtils.animateMove(ANIM_DURATION, 0, goodCircle)
        AnimUtils.animateMove(ANIM_DURATION, 0, badCircle)
        AnimUtils.animateMove(ANIM_DURATION, 0, veryBadCircle)
    }

    fun setOnSelectedMoodAction(action: (CircleMoodBO) -> Unit) {
        onSelectedMoodChange = action
    }
}