package com.jemiola.moodtimeline.views.detailstimelinemood

import com.jemiola.moodtimeline.base.BasePresenter
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class DetailsTimelineMoodPresenter(
    private val view: DetailsTimelineMoodContract.View,
    override val repository: DetailsTimelineMoodRepository
) : BasePresenter(repository), DetailsTimelineMoodContract.Presenter {

    override fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d")
        return date.format(formatter).capitalize()
    }
}