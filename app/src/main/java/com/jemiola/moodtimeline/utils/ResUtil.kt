package com.jemiola.moodtimeline.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build

object ResUtil {

    fun getString(resources: Resources, resourceId: Int): String {
        return resources.getString(resourceId)
    }

    fun getFormattedString(resources: Resources, resourceId: Int, vararg textsToFormat: String?): String {
        return String.format(
            resources.getString(resourceId),
            *textsToFormat
        )
    }

    fun getColor(context: Context?, resourceId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context!!.getColor(resourceId)
        } else {
            context!!.resources.getColor(resourceId)
        }
    }

    fun getColorAsColorStateList(resources: Resources, resourceId: Int): ColorStateList {
        return ColorStateList.valueOf(resources.getColor(resourceId))
    }

    fun getBoolean(resources: Resources, resourceId: Int): Boolean {
        return resources.getBoolean(resourceId)
    }

    fun getDrawable(context: Context?, resourceId: Int): Drawable? {
        return context!!.resources.getDrawable(resourceId, context.theme)
    }

    fun getDimenDp(resources: Resources, resourceId: Int): Int {
        return (resources.getDimension(resourceId) / resources.displayMetrics.density).toInt()
    }
}
