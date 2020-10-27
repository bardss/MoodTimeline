package com.jemiola.moodtimeline.views.settings

import android.content.Context
import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.model.data.local.FirstAndLastMoodDateText
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.pdfgenerator.MoodsPdfGenerator
import com.jemiola.moodtimeline.utils.rangepickers.RangeFormatter
import org.threeten.bp.LocalDate

class SettingsPresenter(
    val view: SettingsContract.View,
    override val repository: SettingsRepository
) : BasePresenter(repository), SettingsContract.Presenter {

    private val pdfGenerator = MoodsPdfGenerator()
    private val rangeFormatter = RangeFormatter()

    override fun generatePdfWithAllMoods(context: Context) {
        val callback = createRepositoryCallback<List<TimelineMoodBOv2>>(
            onSuccessAction = { pdfGenerator.generatePdf(context, it) },
            onErrorAction = { }
        )
        repository.getAllTimetableMoods(callback)
    }

    override fun generatePdfWithRangeMoods(context: Context) {
        val fromDateText = view.getFromRangeText()
        val toDateText = view.getToRangeText()
        val formatter = rangeFormatter.getDefaultSearchDateFormatter()
        val fromDate = LocalDate.parse(fromDateText, formatter)
        val toDate = LocalDate.parse(toDateText, formatter)
        val callback = createRepositoryCallback<List<TimelineMoodBOv2>>(
            onSuccessAction = { pdfGenerator.generatePdf(context, it) },
            onErrorAction = { }
        )
        repository.getAllTimetableMoodsWithRange(callback, fromDate, toDate)
    }


    override fun setMinMaxRangeDates() {
        val callback = createRepositoryCallback<FirstAndLastMoodDateText>(
            onSuccessAction = {
                view.setFromRangeText(it.firstMoodDateText)
                view.setToRangeText(it.lastMoodDateText)
                view.setupRangeEditTexts()
            },
            onErrorAction = { }
        )
        repository.getMoodsFirstAndLastDate(callback)
    }
}