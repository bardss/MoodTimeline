package com.jemiola.moodtimeline.views.mooddetails

import com.jemiola.moodtimeline.base.BasePresenter
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class MoodDetailsPresenter(
    private val view: MoodDetailsContract.View,
    override val repository: MoodDetailsRepository
) : BasePresenter(repository), MoodDetailsContract.Presenter {

    override fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d").withLocale(Locale.ENGLISH)
        return date.format(formatter).capitalize()
    }
}