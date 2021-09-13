package com.jemiola.moodtimeline.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DelayedTask(private val scope: CoroutineScope) {

    fun run(delayTime: Int, action: () -> Unit) {
        scope.launch {
            delay(delayTime.toLong())
            action()
        }
    }
}