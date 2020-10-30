package com.jemiola.moodtimeline.utils

import android.app.Activity
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jemiola.moodtimeline.R

private const val LOADING_TIMEOUT = 10000L

class LoadingHandler(activity: Activity) {

    private val loadingView: View by lazy {
        LayoutInflater.from(activity).inflate(R.layout.layout_loading, null)
    }
    private val dialogRootView: ViewGroup by lazy {
        activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
    }
    private val delayHandler = Handler()
    private var loaderStartedCount = 0

    fun showLoading(infinite: Boolean) {
        loaderStartedCount += 1
        if (loadingView.parent == null) {
            dialogRootView.addView(loadingView)
            loadingView.visibility = View.VISIBLE
            if (!infinite) {
                delayHandler.postDelayed({
                    stopLoading()
                }, LOADING_TIMEOUT)
            }
        }
    }

    fun stopLoading() {
        if (loaderStartedCount > 0) loaderStartedCount -= 1
        if (loadingView.parent != null && loaderStartedCount == 0) {
            loadingView.visibility = View.GONE
            dialogRootView.removeView(loadingView)
        }
    }
}