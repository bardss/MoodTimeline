package com.jemiola.moodtimeline.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.utils.ResUtil

class RalewayEditText : AppCompatEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        typeface = Typeface.createFromAsset(context.assets, "fonts/Raleway-Regular.ttf")
        backgroundTintList = ResUtil.getColorAsColorStateList(R.color.colorAccent)
        setHintTextColor(ResUtil.getColorAsColorStateList(R.color.colorAccent))
        setTextColor(ResUtil.getColor(R.color.colorText))
    }
}