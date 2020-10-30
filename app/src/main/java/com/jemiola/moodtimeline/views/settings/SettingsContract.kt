package com.jemiola.moodtimeline.views.settings

import android.content.Context

interface SettingsContract {
    interface Presenter {
        fun generatePdfWithAllMoods(context: Context)
        fun generatePdfWithRangeMoods(context: Context)
        fun setMinMaxRangeDates()
    }

    interface View {
        fun setFromRangeText(date: String)
        fun setToRangeText(date: String)
        fun getFromRangeText(): String
        fun getToRangeText(): String
        fun setupRangeEditTexts()
        fun startLoading()
        fun stopLoading()
        fun showGeneratePdfSuccessDialog()
        fun toggleExportMoodsDialogVisibility()
    }
}