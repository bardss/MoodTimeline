package com.jemiola.moodtimeline.utils

import org.threeten.bp.Clock
import org.threeten.bp.ZoneId

object DefaultTime {

    fun getClock(): Clock {
        return Clock.systemDefaultZone()
    }

    fun getZone(): ZoneId {
        return ZoneId.systemDefault()
    }
}