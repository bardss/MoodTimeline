package com.jemiola.moodtimeline.views.detailstimelinemood

import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import org.threeten.bp.LocalDate

interface DetailsTimelineMoodContract {
    interface Presenter {
        fun getFormattedDate(date: LocalDate): String
    }

    interface View
}