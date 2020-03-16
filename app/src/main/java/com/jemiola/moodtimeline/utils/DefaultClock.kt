package com.jemiola.moodtimeline.utils

import org.threeten.bp.Clock

object DefaultClock {

    fun getClock(): Clock {
        return Clock.systemDefaultZone()
    }
}