package com.jemiola.moodtimeline.views.settings.generatepdf

import android.content.Context
import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.model.data.local.FirstAndLastMoodDateText
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.pdfgenerator.MoodsPdfGenerator
import com.jemiola.moodtimeline.utils.rangepickers.RangeFormatter
import org.threeten.bp.LocalDate

class GeneratePdfPresenter(
    val view: GeneratePdfContract.View,
    override val repository: GeneratePdfRepository
) : BasePresenter(repository), GeneratePdfContract.Presenter {

    private val pdfGenerator = MoodsPdfGenerator()
    private val rangeFormatter = RangeFormatter()

    override fun generatePdfWithAllMoods(context: Context) {
        view.showGeneratingPdfLoading()
        val callback = createRepositoryCallback<List<TimelineMoodBOv2>>(
            onSuccessAction = {
                val pdfFile = pdfGenerator.generatePdf(context, it)
                view.stopGeneratingPdfLoading()
                view.showGeneratePdfSuccessDialog(pdfFile)
            },
            onErrorAction = {
                view.stopGeneratingPdfLoading()
            }
        )
        repository.getAllTimetableMoods(callback)
    }

    override fun generatePdfWithRangeMoods(context: Context) {
        view.showGeneratingPdfLoading()
        val fromDateText = view.getFromRangeText()
        val toDateText = view.getToRangeText()
        val formatter = rangeFormatter.getDefaultSearchDateFormatter()
        val fromDate = LocalDate.parse(fromDateText, formatter)
        val toDate = LocalDate.parse(toDateText, formatter)
        val callback = createRepositoryCallback<List<TimelineMoodBOv2>>(
            onSuccessAction = {
                val pdfFile = pdfGenerator.generatePdf(context, it)
                view.stopGeneratingPdfLoading()
                view.showGeneratePdfSuccessDialog(pdfFile)
            },
            onErrorAction = {
                view.stopGeneratingPdfLoading()
            }
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