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

    fun animateAlpha(duration2: Int, from: Float, to: Float, view: View){
//        val alphaAnimation = AlphaAnimation(from, to).apply {
//            this.duration = duration.toLong()
//        }
//        view.startAnimation(alphaAnimation)
        view.animate().apply {
            alpha(to)
            duration = duration2.toLong()
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