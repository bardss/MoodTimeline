package com.jemiola.moodtimeline.utils.pdfgenerator

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import java.util.*

val PDF_GENERATOR_ENVIRONMENT_DIR: String = Environment.DIRECTORY_DOCUMENTS

class PdfGeneratorFileManager {
    fun createFileWithTimeStamp(context: Context, from: LocalDate, to: LocalDate): File {
        val storageDir = context.getExternalFilesDir(PDF_GENERATOR_ENVIRONMENT_DIR)
        val fileNameWithTimeStamp =
            createFileNameWithTimeStamp(
                from,
                to
            )
        return File.createTempFile(fileNameWithTimeStamp, ".pdf", storageDir)
    }

    public fun createFileNameWithTimeStamp(from: LocalDate, to: LocalDate): String {
        val timeNow = LocalDateTime.now()
        val timeStampFormatter =
            DateTimeFormatter.ofPattern("A").withLocale(Locale.ENGLISH)
        val timeStamp = timeNow.format(timeStampFormatter)
        val rangeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy").withLocale(Locale.ENGLISH)
        val fromTimeText = from.format(rangeFormatter)
        val toTimeText = to.format(rangeFormatter)
        return "mood_timeline_from_${fromTimeText}_to_${toTimeText}_$timeStamp"
    }

    fun getUriToPdf(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )
    }
}