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

    fun fadeIn(duration: Int, vararg views: View) {
        for (view in views) {
            view.alpha = 0f
            view.visibility = View.VISIBLE
            view.animate()
                .alpha(1f)
                .setDuration(duration.toLong())
                .setListener(null)
        }
    }
}