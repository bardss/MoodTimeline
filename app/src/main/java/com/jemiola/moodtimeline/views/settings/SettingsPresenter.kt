package com.jemiola.moodtimeline.views.settings

import android.content.Context
import com.jemiola.moodtimeline.base.BasePresenter
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.pdfgenerator.MoodsPdfGenerator

class SettingsPresenter(
    val view: SettingsContract.View,
    override val repository: SettingsRepository
) : BasePresenter(repository), SettingsContract.Presenter {

    private val pdfGenerator = MoodsPdfGenerator()


    override fun generateMoodsPdf(context: Context) {
        val callback = createRepositoryCallback<List<TimelineMoodBOv2>>(
            onSuccessAction = { pdfGenerator.generatePdf(context, it) },
            onErrorAction = { }
        )
        repository.getTimetableMoods(callback)
    }
}