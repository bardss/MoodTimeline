package com.jemiola.moodtimeline.views.settings.generatepdf

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.jemiola.moodtimeline.utils.pdfgenerator.MoodsPdfGenerator
import com.jemiola.moodtimeline.utils.rangepickers.RangeFormatter
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.LocalDate

const val INPUT_FROM_KEY = "from"
const val INPUT_TO_KEY = "to"
const val OUTPUT_PDF_PATH_KEY = "pdf_path"

class GeneratePdfWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val generatePdfRepository: GeneratePdfRepository by inject()

    override suspend fun doWork(): Result {
        val rangeFormatter = RangeFormatter().getDefaultSearchDateFormatter()
        val fromDateString = inputData.getString(INPUT_FROM_KEY)
        val toDateString = inputData.getString(INPUT_TO_KEY)
        val fromDate = LocalDate.parse(fromDateString, rangeFormatter)
        val toDate = LocalDate.parse(toDateString, rangeFormatter)
        val moods = generatePdfRepository.getAllTimetableMoodsWithRangeSuspend(fromDate, toDate)
        val pdfFile = MoodsPdfGenerator().generatePdf(context, moods)
        return Result.success(workDataOf(OUTPUT_PDF_PATH_KEY to pdfFile.path))
    }
}