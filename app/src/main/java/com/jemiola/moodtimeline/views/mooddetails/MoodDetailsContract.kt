package com.jemiola.moodtimeline.views.mooddetails

import org.threeten.bp.LocalDate

interface MoodDetailsContract {
    interface Presenter {
        fun getFormattedDate(date: LocalDate): String
    }

    interface View
}