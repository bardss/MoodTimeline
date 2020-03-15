package com.jemiola.moodtimeline.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView


class ComfortaRegularTextView : AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        typeface = Typeface.createFromAsset(context.assets, "fonts/Comfortaa-Regular.ttf")
    }
}