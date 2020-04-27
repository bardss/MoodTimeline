package com.jemiola.moodtimeline.views.calendar

import com.jemiola.moodtimeline.model.data.local.CircleMoodBO

interface CalendarContract {
    interface Presenter {
        fun setupCalendar()
        fun openPreviousMonth()
        fun openNextMonth()
    }

    interface View {
        fun setMonthName(format: String)
        fun addNotCurrentMonthDay(day: Int)
        fun addCurrentMonthDefaultDay(day: Int)
        fun addCurrentMonthMoodDay(day: Int, mood: CircleMoodBO)
        fun clearDaysInCalendar()
    }
}