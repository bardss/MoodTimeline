package com.jemiola.moodtimeline.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd


object AnimUtils {

    fun animateMove(duration: Int, move: Int, view: View, doOnEnd: () -> Unit = {}) {
        val buttonAnimator =
            ObjectAnimator.ofFloat(view, "translationX", move.toFloat())
        buttonAnimator.duration = duration.toLong()
        buttonAnimator.doOnEnd {
            doOnEnd.invoke()
        }
        buttonAnimator.start()
    }

    fun animateAlpha(durationToSet: Int, to: Float, vararg views: View) {
        views.forEach {  view ->
            view.animate().apply {
                alpha(to)
                duration = durationToSet.toLong()
                setListener(
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            view.alpha = to
                            super.onAnimationEnd(animation)
                        }
                    }
                )
                start()
            }
        }
    }

    fun fadeIn(duration: Int, vararg views: View) {
        fadeIn(duration, {}, *views)
    }

    fun fadeOut(duration: Int, vararg views: View) {
        fadeOut(duration, {}, *views)
    }

    fun fadeIn(duration: Int, doOnEnd: () -> Unit = {}, vararg views: View) {
        for (view in views) {
            view.alpha = 0f
            view.visibility = View.VISIBLE
            view.animate()
                .alpha(1f)
                .setDuration(duration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        doOnEnd.invoke()
                    }
                })
        }
    }

    fun fadeOut(duration: Int, doOnEnd: () -> Unit = {}, vararg views: View) {
        for (view in views) {
            view.alpha = 1f
            view.visibility = View.VISIBLE
            view.animate()
                .alpha(0f)
                .setDuration(duration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        doOnEnd.invoke()
                    }
                })
        }
    }
}