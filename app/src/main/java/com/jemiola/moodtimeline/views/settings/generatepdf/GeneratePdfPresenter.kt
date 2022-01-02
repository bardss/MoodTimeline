package com.jemiola.moodtimeline.views.settings.generatepdf

import com.jemiola.moodtimeline.base.BasePresenterMVP
import com.jemiola.moodtimeline.model.data.local.FirstAndLastMoodDateText

class GeneratePdfPresenter(
    val view: GeneratePdfContract.View,
    override val repository: GeneratePdfRepository
) : BasePresenterMVP(repository), GeneratePdfContract.Presenter {

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