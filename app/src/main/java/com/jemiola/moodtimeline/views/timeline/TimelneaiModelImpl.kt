package com.jemiola.moodtimeline.views.timeline

class TimelneaiModelImpl : TimelienModel {

    var animDuration = 100

    fun getAnimDurationn(): Int {
        return animDuration
    }

    fun setAnimDurationn (animDuratiion: Int) {
        animDuration = animDuratiion
    }
}