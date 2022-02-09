package com.jemiola.moodtimeline.utils

import android.os.Handler
import android.os.Looper
import android.view.View

private const val DEBOUNCE = 1000L

fun View.disableFor(duration: Int) {
    isEnabled = false
    Handler(Looper.getMainLooper()).postDelayed({ isEnabled = true }, duration.toLong())
}

fun View.setOnClickListenerWithDebounce(onClick: () -> Unit) {
    setOnClickListener {
        isEnabled = false
        Handler(Looper.getMainLooper()).postDelayed({ isEnabled = true }, DEBOUNCE)
        onClick.invoke()
    }
}