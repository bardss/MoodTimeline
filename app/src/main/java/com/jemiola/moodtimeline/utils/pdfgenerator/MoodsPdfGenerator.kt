package com.jemiola.moodtimeline.utils.pdfgenerator

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.model.data.local.MoodPdfPageInfo
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.ImageUtils
import com.jemiola.moodtimeline.utils.ResUtil
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

private const val PAGE_WIDTH = 595
private const val PAGE_HEIGHT = 842
private const val PAGE_MARGIN_TOP = 68f
private const val PAGE_MARGIN_BOTTOM = 58f

private const val PICTURE_MAX_WIDTH = 200
private const val PICTURE_MAX_HEIGHT = 150

private const val TEXT_SIZE_TITLE = 32f
private const val TEXT_SIZE_CONTENT = 16f
private const val MARGIN_TOP_CONTENT = 128f
private const val MARGIN_SIDE_CONTENT = 32f
private const val SIZE_MOOD_BITMAP = 22
private const val SPACE_BETWEEN_PICTURES = 12f

class MoodsPdfGenerator {

    private val pdfGeneratorFileManager = PdfGeneratorFileManager()

    fun generatePdf(context: Context, moods: List<TimelineMoodBOv2>) {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        drawMoodsOnPages(context, pageInfo, document, moods)
        savePdfToDirectory(context, document)
    }

    private fun drawMoodsOnPages(
        context: Context,
        pageInfo: PdfDocument.PageInfo,
        document: PdfDocument,
        moods: List<TimelineMoodBOv2>
    ) {
        val resources = context.resources
        var currentMoodPage = MoodPdfPageInfo(document.startPage(pageInfo), MARGIN_TOP_CONTENT)
        drawMoodTimelineTitle(resources, currentMoodPage.page)
        val paint = Paint().apply {
            textAlign = Paint.Align.LEFT
            textSize = TEXT_SIZE_CONTENT
            typeface = Typeface.createFromAsset(context.assets, "fonts/Raleway-Regular.ttf")
        }
        moods.forEach { mood ->
            // draw Divider Line
            val estimatedLineHeightLine = TEXT_SIZE_CONTENT * 2
            currentMoodPage = checkIfNextElementFitsAndIfNotReturnNewPage(
                estimatedLineHeightLine, currentMoodPage, pageInfo, document
            )
            val lineHeight = drawLineAndReturnLineHeight(currentMoodPage, pageInfo, paint)
            currentMoodPage = increasePositionInPage(currentMoodPage, lineHeight)

            // draw Date
            val estimatedLineHeightDate = TEXT_SIZE_CONTENT * 2
            currentMoodPage = checkIfNextElementFitsAndIfNotReturnNewPage(
                estimatedLineHeightDate, currentMoodPage, pageInfo, document
            )
            val dateLineHeight = drawDateAndReturnDateLineHeight(currentMoodPage, mood, paint)
            currentMoodPage = increasePositionInPage(currentMoodPage, dateLineHeight)

            // draw Mood
            val estimatedLineHeightMood = TEXT_SIZE_CONTENT * 2
            currentMoodPage = checkIfNextElementFitsAndIfNotReturnNewPage(
                estimatedLineHeightMood, currentMoodPage, pageInfo, document
            )
            val moodLineHeight =
                drawMoodAndReturnMoodLineHeight(currentMoodPage, resources, mood, paint)
            currentMoodPage = increasePositionInPage(currentMoodPage, moodLineHeight)

            // draw Notes
            val estimatedLineHeightNotes = estimateNoteSpace(mood.note)
            currentMoodPage = checkIfNextElementFitsAndIfNotReturnNewPage(
                estimatedLineHeightNotes, currentMoodPage, pageInfo, document
            )
            val noteLinesHeight =
                drawNoteTextAndReturnNextLinePosition(currentMoodPage, mood, paint)
            currentMoodPage = increasePositionInPage(currentMoodPage, noteLinesHeight)

            // draw Pictures
            mood.picturesPaths.filter { it.isNotEmpty() }.forEach {
                val estimatedPictureHeight = estimatePicturesSpace(it)
                currentMoodPage = checkIfNextElementFitsAndIfNotReturnNewPage(
                    estimatedPictureHeight, currentMoodPage, pageInfo, document
                )
                val picturesHeight =
                    drawPictureAndReturnNextLinePosition(currentMoodPage, it, paint)
                currentMoodPage = increasePositionInPage(currentMoodPage, picturesHeight)
            }
        }
        document.finishPage(currentMoodPage.page)
    }

    private fun checkIfNextElementFitsAndIfNotReturnNewPage(
        estimatedLineHeight: Float,
        moodPdfPage: MoodPdfPageInfo,
        pageInfo: PdfDocument.PageInfo,
        document: PdfDocument
    ): MoodPdfPageInfo {
        var page = moodPdfPage.page
        var position = moodPdfPage.currentPosition
        if (position + estimatedLineHeight > (PAGE_HEIGHT - PAGE_MARGIN_BOTTOM)) {
            document.finishPage(moodPdfPage.page)
            position = MARGIN_TOP_CONTENT
            page = document.startPage(pageInfo)
        }
        return MoodPdfPageInfo(page, position)
    }

    private fun increasePositionInPage(
        moodPdfPage: MoodPdfPageInfo,
        newPosition: Float
    ): MoodPdfPageInfo {
        return moodPdfPage.copy(currentPosition = moodPdfPage.currentPosition + newPosition)
    }

    private fun drawLineAndReturnLineHeight(
        moodPdfPage: MoodPdfPageInfo,
        pageInfo: PdfDocument.PageInfo,
        paint: Paint
    ): Float {
        moodPdfPage.page.canvas.drawLine(
            MARGIN_SIDE_CONTENT,
            moodPdfPage.currentPosition,
            pageInfo.pageWidth - MARGIN_SIDE_CONTENT,
            moodPdfPage.currentPosition,
            paint
        )
        return TEXT_SIZE_CONTENT * 2
    }

    private fun drawDateAndReturnDateLineHeight(
        moodPdfPage: MoodPdfPageInfo,
        mood: TimelineMoodBOv2,
        paint: Paint
    ): Float {
        val dateValue = getFormatterDate(mood.date)
        val dateText = "Date: $dateValue"
        moodPdfPage.page.canvas.drawText(
            dateText,
            MARGIN_SIDE_CONTENT,
            moodPdfPage.currentPosition,
            paint
        )
        return TEXT_SIZE_CONTENT * 2
    }

    private fun drawNoteTextAndReturnNextLinePosition(
        moodPdfPage: MoodPdfPageInfo,
        mood: TimelineMoodBOv2,
        paint: Paint
    ): Float {
        var textPositionTaken = 0f
        var linePosition = moodPdfPage.currentPosition
        if (mood.note.isEmpty()) {
            val noteText = "Note:  - "
            moodPdfPage.page.canvas.drawText(noteText, MARGIN_SIDE_CONTENT, linePosition, paint)
        } else {
            val noteSplitIntoLines = splitNoteIntoLines(mood.note)
            var noteText: String
            noteSplitIntoLines.forEachIndexed { i, text ->
                noteText = if (i == 0) "Note: $text" else text
                moodPdfPage.page.canvas.drawText(noteText, MARGIN_SIDE_CONTENT, linePosition, paint)
                val takenSpace = TEXT_SIZE_CONTENT * 2
                linePosition += takenSpace
                textPositionTaken += takenSpace
            }
        }
        return textPositionTaken
    }

    private fun drawMoodAndReturnMoodLineHeight(
        moodPdfPage: MoodPdfPageInfo,
        resources: Resources,
        mood: TimelineMoodBOv2,
        paint: Paint
    ): Float {
        val moodBitmap = getMoodBitmap(resources, mood)
        if (moodBitmap != null) {
            moodPdfPage.page.canvas.drawText(
                "Mood: ",
                MARGIN_SIDE_CONTENT,
                moodPdfPage.currentPosition,
                paint
            )
            moodPdfPage.page.canvas.drawBitmap(
                moodBitmap,
                MARGIN_SIDE_CONTENT * 3f,
                moodPdfPage.currentPosition - 18f,
                paint
            )
        }
        return TEXT_SIZE_CONTENT * 2
    }

    private fun drawPictureAndReturnNextLinePosition(
        moodPdfPage: MoodPdfPageInfo,
        picturePath: String,
        paint: Paint
    ): Float {
        val pictureBitmap = BitmapFactory.decodeFile(picturePath)
        val scaledBitmap = ImageUtils.resizeBitmapWhenTooLarge(
            pictureBitmap,
            PICTURE_MAX_WIDTH,
            PICTURE_MAX_HEIGHT
        )
        moodPdfPage.page.canvas.drawBitmap(
            scaledBitmap,
            MARGIN_SIDE_CONTENT,
            moodPdfPage.currentPosition,
            paint
        )
        return scaledBitmap.height + SPACE_BETWEEN_PICTURES * 2
    }

    private fun getMoodBitmap(resources: Resources, mood: TimelineMoodBOv2): Bitmap? {
        val circleMoodDrawableId = mood.circleMood.moodDrawable
        val moodDrawable = ResUtil.getDrawable(resources, circleMoodDrawableId)
        return moodDrawable?.let { getBitmapFromVectorDrawable(moodDrawable) }
    }

    private fun getBitmapFromVectorDrawable(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            SIZE_MOOD_BITMAP,
            SIZE_MOOD_BITMAP,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun getFormatterDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy").withLocale(Locale.ENGLISH)
        return date.format(formatter)
    }

    private fun drawMoodTimelineTitle(resources: Resources, page: PdfDocument.Page) {
        val paint = Paint()
        val pageCanvas = page.canvas
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = TEXT_SIZE_TITLE
        val appName = ResUtil.getString(resources, R.string.app_name)
        val xTitlePlace = (page.info.pageWidth / 2).toFloat()
        pageCanvas.drawText(appName, xTitlePlace, PAGE_MARGIN_TOP, paint)
    }

    private fun savePdfToDirectory(context: Context, document: PdfDocument) {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        try {
            storageDir?.let {
                val file = pdfGeneratorFileManager.createFileWithTimeStamp(
                    storageDir,
                    LocalDate.now(),
                    LocalDate.now().plusDays(1)
                )
                val fos = FileOutputStream(file)
                document.writeTo(fos)
                document.close()
                fos.close()
            }
        } catch (e: IOException) {
            throw RuntimeException("Error generating file", e)
        }
    }

    private fun splitNoteIntoLines(note: String): List<String> {
        return note.split("\n")
    }

    private fun estimateNoteSpace(note: String): Float {
        return note.split("\n").count() * TEXT_SIZE_CONTENT * 2
    }

    private fun estimatePicturesSpace(picturePath: String): Float {
        val pictureBitmap = BitmapFactory.decodeFile(picturePath)
        val resizedBitmap = ImageUtils.resizeBitmapWhenTooLarge(
            pictureBitmap,
            PICTURE_MAX_WIDTH,
            PICTURE_MAX_HEIGHT
        )
        return resizedBitmap.height + SPACE_BETWEEN_PICTURES
    }

}