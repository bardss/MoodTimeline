package com.jemiola.moodtimeline.views.settings

import android.content.Context

interface SettingsContract {
    interface Presenter {
        fun generateMoodsPdf(context: Context)
    }

    interface View {
    }
}