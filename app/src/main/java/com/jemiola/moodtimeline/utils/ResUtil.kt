package com.jemiola.moodtimeline.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import com.jemiola.moodtimeline.base.BaseApplication

object ResUtil {

    fun getString(resourceId: Int): String {
        return BaseApplication.context.getString(resourceId)
    }

    fun getFormattedString(resourceId: Int, vararg textsToFormat: String?): String {
        return String.format(
            BaseApplication.context.getString(resourceId),
            *textsToFormat
        )
    }

    fun getColor(resourceId: Int): Int {
        return BaseApplication.context.resources.getColor(resourceId)
    }

    fun getColorAsColorStateList(resourceId: Int): ColorStateList {
        return ColorStateList.valueOf(BaseApplication.context.resources.getColor(resourceId))
    }

    fun getBoolean(resourceId: Int): Boolean {
        return BaseApplication.context.resources.getBoolean(resourceId)
    }

    fun getDrawable(resourceId: Int): Drawable? {
        return BaseApplication.context.getDrawable(resourceId)
    }

}
