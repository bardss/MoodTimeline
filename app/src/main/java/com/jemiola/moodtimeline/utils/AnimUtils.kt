package com.jemiola.moodtimeline.utils

import android.animation.ObjectAnimator
import android.view.View

object AnimUtils {

    fun animateMove(duration: Int, move: Int, view: View) {
        val buttonAnimator =
            ObjectAnimator.ofFloat(view, "translationX", move.toFloat())
        buttonAnimator.duration = duration.toLong()
        buttonAnimator.start()
    }
}