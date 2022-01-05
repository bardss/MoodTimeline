package com.jemiola.moodtimeline.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.res.ResourcesCompat
import com.jemiola.moodtimeline.base.BaseApplication
import com.jemiola.moodtimeline.customviews.Typefaces

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

    fun getDrawable(context: Context, resourceId: Int): Drawable? {
        return ResourcesCompat.getDrawable(context.resources, resourceId, context.theme)
    }

    fun getDimenDp(resources: Resources, resourceId: Int): Int {
        return (resources.getDimension(resourceId) / resources.displayMetrics.density).toInt()
    }

    fun getDimenSp(resources: Resources, resourceId: Int): Float {
        return resources.getDimension(resourceId)
    }

    fun getTypeface(typeface: Typefaces): Typeface {
        return Typeface.createFromAsset(BaseApplication.context.assets, "fonts/${typeface.path}")
    }
}
