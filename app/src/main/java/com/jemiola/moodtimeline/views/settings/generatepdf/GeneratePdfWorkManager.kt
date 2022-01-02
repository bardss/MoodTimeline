package com.jemiola.moodtimeline.views.settings.generatepdf

import android.content.Context
import androidx.work.*
import com.jemiola.moodtimeline.base.BaseFragmentMVP
import java.io.File

private const val WORKER_TAG = "generate_pdf_worker_tag"
private const val WORK_NAME = "generate_pdf_work_name"

class GeneratePdfWorkManager(context: Context) {
    private val workManager by lazy { WorkManager.getInstance(context) }
    private val constraints = Constraints.Builder()
        .setRequiresStorageNotLow(true)
        .build()

    fun runGeneratePdfRequest(
        lifecycleOwner: BaseFragmentMVP,
        fromDateString: String,
        toDateString: String,
        onGeneratePdfFinish: (File) -> Unit,
        onGeneratePdfError: () -> Unit
    ) {
        val inputData = Data.Builder()
            .putString(INPUT_FROM_KEY, fromDateString)
            .putString(INPUT_TO_KEY, toDateString)
            .build()
        val generatePdfWorker = OneTimeWorkRequestBuilder<GeneratePdfWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .addTag(WORKER_TAG)
            .build()
        workManager.enqueueUniqueWork(
            WORK_NAME,
            ExistingWorkPolicy.KEEP,
            generatePdfWorker
        )
        workManager.getWorkInfoByIdLiveData(generatePdfWorker.id)
            .observe(lifecycleOwner, { info ->
                if (info != null) {
                    when (info.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            val outputPath = info.outputData.getString(OUTPUT_PDF_PATH_KEY)
                            if (outputPath != null) {
                                val pdfFile = File(outputPath)
                                onGeneratePdfFinish(pdfFile)
                            }
                        }
                        WorkInfo.State.FAILED -> onGeneratePdfError()
                    }

                }
            })
    }
}