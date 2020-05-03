package com.jemiola.moodtimeline.views.detailstimelinemood

import com.jemiola.moodtimeline.base.BasePresenter
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class DetailsTimelineMoodPresenter(
    private val view: DetailsTimelineMoodContract.View,
    override val repository: DetailsTimelineMoodRepository
) : BasePresenter(repository), DetailsTimelineMoodContract.Presenter {

    override fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d").withLocale(Locale.ENGLISH)
        return date.format(formatter).capitalize()
    }
}