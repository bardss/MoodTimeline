package com.jemiola.moodtimeline.utils

import android.os.Handler
import android.view.View

fun View.disableFor(duration: Int) {
    isEnabled = false
    Handler().postDelayed({ isEnabled = true }, duration.toLong())
}