package com.jemiola.moodtimeline.views.calendar

import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.format.TextStyle
import java.util.*

class CalendarPresenter(
    public val view: CalendarContract.View,
    override val repository: CalendarRepository
) : BasePresenter(repository), CalendarContract.Presenter {

    override fun setupCalendar() {
        val callback = createRepositoryCallback<List<TimelineMoodBOv2>>(
            onSuccessAction = { setupCalendarView(it) },
            onErrorAction = {}
        )
        repository.getCurrentMonthMoods(callback)
    }

    public fun setupCalendarView(moods: List<TimelineMoodBOv2>) {
        view.hideCalendar {
            setupMonthText()
            setupMonthDays(moods)
            view.showCalendar()
        }
    }

    public fun setupMonthText() {
        val dateNow = repository.currentMonthDate
        val month = getFormattedMonth(dateNow.month)
        month?.let { monthText ->
            view.setMonthName(monthText)
        }
    }

    public fun setupMonthDays(moods: List<TimelineMoodBOv2>) {
        view.clearDaysInCalendar()
        val currentDate = repository.currentMonthDate
        val numberOfPreviousMonthDays = getPreviousMonthLength(currentDate)
        val firstDayToShowFromPreviousMonth = getFirstDayToShowFromPreviousMonth(currentDate)
        for (previousMonthDay in firstDayToShowFromPreviousMonth..numberOfPreviousMonthDays) {
            view.addNotCurrentMonthDay(previousMonthDay)
        }
        val currentMonthDays = currentDate.lengthOfMonth()
        for (currentMonthDay in 1..currentMonthDays) {
            if (doesDayHaveMood(currentMonthDay, moods)) {
                val mood = getMoodForCalendarDay(currentMonthDay, moods)
                view.addCurrentMonthMoodDay(currentMonthDay, mood)
            } else view.addCurrentMonthDefaultDay(currentMonthDay)
        }
        val daysToShowFromNextMonth = getNumbersOfDaysToShowFromNextMonth(currentDate)
        for (nextMonthDay in 1..daysToShowFromNextMonth) {
            view.addNotCurrentMonthDay(nextMonthDay)
        }
        view.requestCalendarLayout()
    }

    public fun getMoodForCalendarDay(
        checkedDay: Int,
        moods: List<TimelineMoodBOv2>
    ): TimelineMoodBOv2 {
        val currentMonth = repository.currentMonthDate.monthValue
        val currentYear = repository.currentMonthDate.year
        val checkedDate = LocalDate.of(currentYear, currentMonth, checkedDay)
        return moods.first {
            it.date == checkedDate
        }
    }

    public fun doesDayHaveMood(checkedDay: Int, moods: List<TimelineMoodBOv2>): Boolean {
        val currentMonth = repository.currentMonthDate.monthValue
        val currentYear = repository.currentMonthDate.year
        val checkedDate = LocalDate.of(currentYear, currentMonth, checkedDay)
        return moods.any {
            it.date == checkedDate
        }
    }

    public fun getFirstDayToShowFromPreviousMonth(currentDate: LocalDate): Int {
        val firstDayOfMonth = currentDate.withDayOfMonth(1)
        val dayOfWeekOfFirstDayOfMonth = firstDayOfMonth.dayOfWeek.value - 2
        val numberOfPreviousMonthDays = getPreviousMonthLength(currentDate)
        return numberOfPreviousMonthDays - dayOfWeekOfFirstDayOfMonth
    }

    public fun getPreviousMonthLength(currentDate: LocalDate) =
        currentDate.month.minus(1).length(currentDate.isLeapYear)

    public fun getNumbersOfDaysToShowFromNextMonth(currentDate: LocalDate): Int {
        val currentMonthDays = currentDate.lengthOfMonth()
        val lastDayOfMonth = currentDate.withDayOfMonth(currentMonthDays)
        val dayOfWeekOfLastDayOfMonth = lastDayOfMonth.dayOfWeek.value
        return 7 - dayOfWeekOfLastDayOfMonth
    }

    override fun openPreviousMonth() {
        repository.currentMonthDate = repository.currentMonthDate.minusMonths(1)
        setupCalendar()
    }

    override fun openNextMonth() {
        repository.currentMonthDate = repository.currentMonthDate.plusMonths(1)
        setupCalendar()
    }

    public fun getFormattedMonth(month: Month) =
        month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
}