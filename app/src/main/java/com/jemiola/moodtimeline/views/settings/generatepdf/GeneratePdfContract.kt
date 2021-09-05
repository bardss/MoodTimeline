package com.jemiola.moodtimeline.views.settings.generatepdf

import android.content.Context
import java.io.File

interface GeneratePdfContract {
    interface Presenter {
        fun setMinMaxRangeDates(onSetMaxMinValuesSuccess: () -> Unit = { })
    }

    interface View {
        fun setFromRangeText(date: String)
        fun setToRangeText(date: String)
        fun getFromRangeText(): String
        fun getToRangeText(): String
        fun setupRangeEditTexts()
        fun showGeneratingPdfLoading()
        fun stopGeneratingPdfLoading()
        fun showGeneratePdfSuccessDialog(pdfFile: File)
        fun hideExportPdfDialog()
    }
}