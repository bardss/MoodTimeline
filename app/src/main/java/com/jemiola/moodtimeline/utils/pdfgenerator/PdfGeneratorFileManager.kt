package com.jemiola.moodtimeline.utils.pdfgenerator

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import java.util.*

class PdfGeneratorFileManager {
    fun createFileWithTimeStamp(storageDir: File, from: LocalDate, to: LocalDate): File {
        val fileNameWithTimeStamp =
            createFileNameWithTimeStamp(
                from,
                to
            )
        return File.createTempFile(fileNameWithTimeStamp, ".pdf", storageDir)
    }

    private fun createFileNameWithTimeStamp(from: LocalDate, to: LocalDate): String {
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
}