package com.jemiola.moodtimeline.utils

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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

}