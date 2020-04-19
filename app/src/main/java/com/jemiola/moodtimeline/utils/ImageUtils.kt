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
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

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

    fun getBitmapDrawableFromPath(
        path: String?,
        quality: Int = 25,
        addingPicture: Boolean = false
    ): RoundedBitmapDrawable? {
        val file = File(path)
        val pictureBitmap = BitmapFactory.decodeFile(path)
        if (pictureBitmap != null) {
            val rotateBitmap = performRotation(file, pictureBitmap)
            if (addingPicture) {
                val resizedBitmap = resizeBitmap(rotateBitmap)
                resizedBitmap.saveBitmap(file, quality)
            }
            val drawable = RoundedBitmapDrawableFactory.create(
                BaseApplication.context.resources,
                rotateBitmap
            )
            drawable.cornerRadius = 20f
            return drawable
        }
        return null
    }

    private fun resizeBitmap(source: Bitmap): Bitmap {
        return Bitmap.createScaledBitmap(
            source,
            source.width / 2,
            source.height / 2,
            true
        )
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

    private fun Bitmap.saveBitmap(file: File, quality: Int) {
        try {
            val fileOutputStream = FileOutputStream(file)
            this.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            Log.e("File not found: ", e.message)
        } catch (e: IOException) {
            Log.e("Error accessing file: ", e.message)
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(angle) }
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

}