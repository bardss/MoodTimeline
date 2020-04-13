package com.jemiola.moodtimeline.utils

import android.database.Cursor
import android.graphics.*
import android.net.Uri
import android.provider.MediaStore
import com.jemiola.moodtimeline.base.BaseApplication

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

    fun getBitmapFromPath(path: String?): Bitmap? {
        val pictureBitmap = BitmapFactory.decodeFile(path)
        return pictureBitmap?.let { roundImageCorners(pictureBitmap) }
    }

    private fun roundImageCorners(bitmap: Bitmap): Bitmap {
        val imageRounded = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(imageRounded)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        canvas.drawRoundRect(
            RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat()),
            40f,
            40f,
            paint
        )
        return imageRounded
    }
}