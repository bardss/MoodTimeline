package com.jemiola.moodtimeline.views.calendar

interface CalendarContract {
    interface Presenter {
        fun setupCalendarView()
        fun openPreviousMonth()
        fun openNextMonth()
    }

    interface View {
        fun setMonthName(format: String)
    }
}