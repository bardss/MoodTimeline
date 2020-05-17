package com.jemiola.moodtimeline.utils

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.jemiola.moodtimeline.base.BaseApplication
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

const val IMAGE_QUALITY_COMPRESS = 30

object ImageUtils {
    fun getPathFromUri(uri: Uri): String? {
        return when {
            uri.toString().contains("content:") -> getRealPathFromURI(
                uri
            )
            uri.toString().contains("file:") -> uri.path
            else -> null
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = BaseApplication.context.contentResolver?.query(
                uri, proj, null, null, null
            )
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            return columnIndex?.let { cursor?.getString(it) }
        } finally {
            cursor?.close()
        }
    }

    fun saveOptimisedPictureAndReturnPath(
        path: String?,
        storageDir: File
    ): String? {
        val file = File(path)
        val pictureBitmap = BitmapFactory.decodeFile(path)
        return if (pictureBitmap != null) {
            val rotateBitmap = performRotation(file, pictureBitmap)
            val resizedBitmap = resizeBitmapWhenTooLarge(rotateBitmap)
            saveBitmap(resizedBitmap, IMAGE_QUALITY_COMPRESS, storageDir)
        } else null
    }

    fun getBitmapDrawableFromPath(path: String?): RoundedBitmapDrawable? {
        val pictureBitmap = BitmapFactory.decodeFile(path)
        return if (pictureBitmap != null) {
            val drawable = RoundedBitmapDrawableFactory.create(
                BaseApplication.context.resources,
                pictureBitmap
            )
            drawable.cornerRadius = 20f
            drawable
        } else null
    }

    private fun resizeBitmapWhenTooLarge(source: Bitmap): Bitmap {
        return if (source.width > 1200 || source.height > 1200) {
            Bitmap.createScaledBitmap(
                source,
                source.width / 2,
                source.height / 2,
                true
            )
        } else source
    }

    private fun performRotation(file: File, bitmap: Bitmap): Bitmap {
        val ei = ExifInterface(file.absolutePath)
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(
                bitmap,
                90f
            )
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(
                bitmap,
                180f
            )
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(
                bitmap,
                270f
            )
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
    }

    private fun saveBitmap(
        bitmapToSave: Bitmap,
        quality: Int,
        storageDir: File
    ): String? {
        try {
            val fileNameWithTimeStamp = createFileNameWithTimeStamp()
            val fileToSaveTo = File.createTempFile(fileNameWithTimeStamp, ".jpg", storageDir)
            val fileOutputStream = FileOutputStream(fileToSaveTo)
            bitmapToSave.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
            fileOutputStream.close()
            return fileToSaveTo.absolutePath
        } catch (e: FileNotFoundException) {
            Log.e("File not found: ", e.message)
        } catch (e: IOException) {
            Log.e("Error accessing file: ", e.message)
        }
        return null
    }

    private fun createFileNameWithTimeStamp(): String {
        val dateTimeNow = LocalDateTime.now(DefaultTime.getClock())
        val formatter = DateTimeFormatter.ofPattern("yyyy_MMM_dd_HH_mm_ss").withLocale(Locale.ENGLISH)
        val timeStamp = dateTimeNow.format(formatter)
        return "mood_timeline_$timeStamp"
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(angle) }
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

}