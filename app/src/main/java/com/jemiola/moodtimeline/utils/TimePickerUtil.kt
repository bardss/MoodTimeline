package com.jemiola.moodtimeline.utils

import android.os.Build
import android.widget.TimePicker

fun TimePicker.getHourCompat(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        hour
    } else {
        currentHour
    }
}

fun TimePicker.getMinuteCompat(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        minute
    } else {
        currentMinute
    }
}