package com.jemiola.moodtimeline.utils.pdfgenerator

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfDocument
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseApplication.Companion.context
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
private const val PAGE_MARGIN_TOP = 38f
private const val PAGE_MARGIN_BOTTOM = 58f

private const val TOP_BAR_HEIGHT = 200f

private const val PICTURE_MAX_WIDTH = 200
private const val PICTURE_MAX_HEIGHT = 150

private const val TEXT_SIZE_CONTENT = 16f
private const val MARGIN_TOP_CONTENT = 128f
private const val MARGIN_SIDE_CONTENT = 32f
private const val SIZE_MOOD_BITMAP = 22
private const val SPACE_BETWEEN_PICTURES = 12f

class MoodsPdfGenerator {

    private val pdfGeneratorFileManager = PdfGeneratorFileManager()
    private val defaultTypeface =
        Typeface.createFromAsset(context.assets, "fonts/Raleway-Regular.ttf")
    private val boldTypeface = Typeface.createFromAsset(context.assets, "fonts/Comfortaa-Bold.ttf")

    fun generatePdf(context: Context, moods: List<TimelineMoodBOv2>) {
        val moodsFromOldestToNewest = moods.reversed()
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        drawMoodsOnPages(context, pageInfo, document, moodsFromOldestToNewest)
        savePdfToDirectory(context, document, moodsFromOldestToNewest)
    }

    private fun drawMoodsOnPages(
        context: Context,
        pageInfo: PdfDocument.PageInfo,
        document: PdfDocument,
        moods: List<TimelineMoodBOv2>
    ) {
        val resources = context.resources
        var currentMoodPage = MoodPdfPageInfo(document.startPage(pageInfo), MARGIN_TOP_CONTENT)
        drawMoodsPdfTopBar(currentMoodPage.page)
        increasePositionInPage(currentMoodPage, TOP_BAR_HEIGHT)
        val paint = Paint().apply {
            textAlign = Paint.Align.LEFT
            textSize = TEXT_SIZE_CONTENT
            typeface = defaultTypeface
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
            position = PAGE_MARGIN_TOP
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
        val dateLabelText = "Date: "
        paint.typeface = boldTypeface
        moodPdfPage.page.canvas.drawText(
            dateLabelText,
            MARGIN_SIDE_CONTENT,
            moodPdfPage.currentPosition,
            paint
        )
        val dateLabelWidth = paint.measureText(dateLabelText)
        paint.typeface = defaultTypeface
        moodPdfPage.page.canvas.drawText(
            dateValue,
            MARGIN_SIDE_CONTENT + dateLabelWidth,
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
        val notesLabelText = "Notes: "
        paint.typeface = boldTypeface
        moodPdfPage.page.canvas.drawText(
            notesLabelText,
            MARGIN_SIDE_CONTENT,
            moodPdfPage.currentPosition,
            paint
        )
        val dateLabelWidth = paint.measureText(notesLabelText)
        paint.typeface = defaultTypeface
        if (mood.note.isEmpty()) {
            val noteText = " - "
            moodPdfPage.page.canvas.drawText(
                noteText,
                MARGIN_SIDE_CONTENT + dateLabelWidth,
                linePosition,
                paint
            )
            textPositionTaken += TEXT_SIZE_CONTENT * 2
        } else {
            val noteSplitIntoLines = splitNoteIntoLines(mood.note)
            noteSplitIntoLines.forEachIndexed { i, text ->
                val sideMargin =
                    if (i == 0) MARGIN_SIDE_CONTENT + dateLabelWidth else MARGIN_SIDE_CONTENT
                moodPdfPage.page.canvas.drawText(text, sideMargin, linePosition, paint)
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
        val moodLabelText = "Mood: "
        if (moodBitmap != null) {
            paint.typeface = boldTypeface
            moodPdfPage.page.canvas.drawText(
                moodLabelText,
                MARGIN_SIDE_CONTENT,
                moodPdfPage.currentPosition,
                paint
            )
            paint.typeface = defaultTypeface
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
        val circleMoodDrawableId = mood.circleMood.moodDrawablePdf
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

    private fun drawMoodsPdfTopBar(page: PdfDocument.Page) {
        val logoBitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.app_logo_text_pdf
        )
        val scaledLogoBitmap = ImageUtils.resizeBitmapWhenTooLarge(logoBitmap, 200, 150)
        val paint = Paint()
        val xLogoPlace = (PAGE_WIDTH / 2) - (scaledLogoBitmap.width / 2)
        page.canvas.drawBitmap(
            scaledLogoBitmap,
            xLogoPlace.toFloat(),
            0f,
            paint
        )
    }

    private fun savePdfToDirectory(
        context: Context,
        document: PdfDocument,
        moods: List<TimelineMoodBOv2>
    ) {
        val from = moods.first().date
        val to = moods.last().date
        try {
            val file = pdfGeneratorFileManager.createFileWithTimeStamp(
                context,
                from,
                to
            )
            val fos = FileOutputStream(file)
            document.writeTo(fos)
            document.close()
            fos.close()
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