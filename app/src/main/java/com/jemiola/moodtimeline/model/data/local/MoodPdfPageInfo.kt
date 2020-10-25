package com.jemiola.moodtimeline.model.data.local

import android.graphics.pdf.PdfDocument

data class MoodPdfPageInfo(
    val page: PdfDocument.Page,
    val currentPosition: Float
)