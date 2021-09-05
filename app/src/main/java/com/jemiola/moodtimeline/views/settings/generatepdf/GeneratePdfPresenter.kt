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

    override fun setMinMaxRangeDates(onSetMaxMinValuesSuccess: () -> Unit) {
        val callback = createRepositoryCallback<FirstAndLastMoodDateText>(
            onSuccessAction = {
                view.setFromRangeText(it.firstMoodDateText)
                view.setToRangeText(it.lastMoodDateText)
                view.setupRangeEditTexts()
                onSetMaxMinValuesSuccess()
            },
            onErrorAction = { }
        )
        repository.getMoodsFirstAndLastDate(callback)
    }
}