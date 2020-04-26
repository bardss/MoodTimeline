package com.jemiola.moodtimeline.views.calendar

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.utils.DefaultTime
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class CalendarPresenter(
    private val view: CalendarContract.View,
    override val repository: CalendarRepository
) : BasePresenter(repository), CalendarContract.Presenter {

    override fun setupCalendarView() {
        setupMonthText()
    }

    private fun setupMonthText() {
        val dateNow = repository.currentMonth
        val monthFormatter = getDefaultMonthFormatter()
        dateNow.format(monthFormatter)?.let { monthText ->
            view.setMonthName(monthText)
        }
    }

    override fun openPreviousMonth() {
        repository.currentMonth = repository.currentMonth.minusMonths(1)
        setupMonthText()
    }

    override fun openNextMonth() {
        repository.currentMonth = repository.currentMonth.plusMonths(1)
        setupMonthText()
    }

    private fun getDefaultMonthFormatter() = DateTimeFormatter.ofPattern("MMMM")
}