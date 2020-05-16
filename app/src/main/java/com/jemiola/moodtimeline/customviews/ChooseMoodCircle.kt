package com.jemiola.moodtimeline.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.ResUtil
import kotlin.properties.Delegates

const val ANIM_DURATION = 300
val MARGIN_BETWEEN_CIRCLES = ResUtil.getDimenDp(R.dimen.choose_mood_circle_margin)

class ChooseMoodCircle : FrameLayout {

    var selectedMood: CircleMoodBO by Delegates.observable(CircleMoodBO.NONE) { _, _, value ->
        mediocreCircle.mood = value
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
        mediocreCircle = findViewById(R.id.mediocreCircle)
        badCircle = findViewById(R.id.badCircle)
        veryBadCircle = findViewById(R.id.veryBadCircle)
        setupMoodColors()
        setupInitialState()
    }

    private fun setupInitialState() {
        mediocreCircle.state = CircleStateBO.EDIT
        veryGoodCircle.state = CircleStateBO.CHOOSE_MOOD
        goodCircle.state = CircleStateBO.CHOOSE_MOOD
        badCircle.state = CircleStateBO.CHOOSE_MOOD
        veryBadCircle.state = CircleStateBO.CHOOSE_MOOD
        mediocreCircle.setOnClickListener { expand() }
        veryGoodCircle.setOnClickListener { selectMood(CircleMoodBO.VERY_GOOD) }
        goodCircle.setOnClickListener { selectMood(CircleMoodBO.GOOD) }
        badCircle.setOnClickListener { selectMood(CircleMoodBO.BAD) }
        veryBadCircle.setOnClickListener { selectMood(CircleMoodBO.VERY_BAD) }
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
            mediocreCircle.state = CircleStateBO.CHOOSE_MOOD
            mediocreCircle.mood = CircleMoodBO.MEDIOCRE
            mediocreCircle.setOnClickListener { selectMood(CircleMoodBO.MEDIOCRE) }
            circleExpandAnimation()
            expanded = true
        }
    }

    private fun selectMood(mood: CircleMoodBO) {
        if (expanded) {
            mediocreCircle.state = CircleStateBO.EDIT
            selectedMood = mood
            mediocreCircle.setOnClickListener { expand() }
            circleCollapseAnimation()
            mediocreCircle.bringToFront()
            expanded = false
        }
    }

    private fun circleExpandAnimation() {
        val distance = mediocreCircle.width + MARGIN_BETWEEN_CIRCLES
        AnimUtils.animateMove(ANIM_DURATION, -(distance * 2), veryBadCircle)
        AnimUtils.animateMove(ANIM_DURATION, -distance, badCircle)
        AnimUtils.animateMove(ANIM_DURATION, distance, goodCircle)
        AnimUtils.animateMove(ANIM_DURATION, distance * 2, veryGoodCircle)
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