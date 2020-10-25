package com.jemiola.moodtimeline.utils

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.jemiola.moodtimeline.base.BaseApplication

object ResUtil {

    fun getString(resources: Resources, resourceId: Int): String {
        return BaseApplication.context.getString(resourceId)
    }

    fun getFormattedString(resources: Resources, resourceId: Int, vararg textsToFormat: String?): String {
        return String.format(
            BaseApplication.context.getString(resourceId),
            *textsToFormat
        )
    }

    fun getColor(resources: Resources, resourceId: Int): Int {
        return BaseApplication.context.resources.getColor(resourceId)
    }

    fun getColorAsColorStateList(resources: Resources, resourceId: Int): ColorStateList {
        return ColorStateList.valueOf(BaseApplication.context.resources.getColor(resourceId))
    }

    fun getBoolean(resources: Resources, resourceId: Int): Boolean {
        return BaseApplication.context.resources.getBoolean(resourceId)
    }

    fun getDrawable(resources: Resources, resourceId: Int): Drawable? {
        return resources.getDrawable(resourceId)
    }

    fun getDimenDp(resourceId: Int): Int {
        val resources = BaseApplication.context.resources
        return (resources.getDimension(resourceId) / resources.displayMetrics.density).toInt()
    }
}
