package com.jemiola.moodtimeline.customviews

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.utils.ResUtil

class MultipartRadioButton : LinearLayout {

    private val buttonValues = mutableListOf<String>()
    private var listener: (String) -> Unit = {}

    init {
        orientation = HORIZONTAL
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        initAttrs(context, attrs)
        addButtons()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.MultipartRadioButton, 0, 0
        ).apply {
            addValueAttr(this, R.styleable.MultipartRadioButton_value1)
            addValueAttr(this, R.styleable.MultipartRadioButton_value2)
            addValueAttr(this, R.styleable.MultipartRadioButton_value3)
            addValueAttr(this, R.styleable.MultipartRadioButton_value4)
            addValueAttr(this, R.styleable.MultipartRadioButton_value5)
            recycle()
        }
    }

    private fun addValueAttr(typedArray: TypedArray, styleableId: Int) {
        val value = typedArray.getString(styleableId)
        if (value != null && value.isNotEmpty()) {
            buttonValues.add(value)
        }
    }

    private fun addButtons() {
        for (i in buttonValues.indices) {
            val button =
                LayoutInflater.from(context)
                    .inflate(R.layout.layout_multipart_button, null) as AppCompatTextView
            button.text = buttonValues[i]
            button.background = getButtonBackgroundUnselected(childCount)
            addView(button)
            setButtonMargins(button, childCount)
            if (i != buttonValues.size - 1) {
                LayoutInflater.from(context)
                    .inflate(R.layout.layout_multipart_button_divider, this)
            }
            (button.layoutParams as LayoutParams).weight = 1f
            button.setOnClickListener {
                changeBackgroundOnClick(button)
                invokeCurrentListener(buttonValues[i])
            }
        }
    }

    private fun getButtonBackgroundUnselected(index: Int): Drawable? {
        val backgroundId = when (index) {
            0 -> R.drawable.multipart_button_left_side_unselected
            getExpectedChildCount() - 1 -> R.drawable.multipart_button_right_side_unselected
            else -> R.drawable.multipart_button_middle_unselected
        }
        return ResUtil.getDrawable(context, backgroundId)
    }

    private fun getButtonBackgroundSelected(index: Int): Drawable? {
        val backgroundId = when (index) {
            0 -> R.drawable.multipart_button_left_side_selected
            getExpectedChildCount() - 1 -> R.drawable.multipart_button_right_side_selected
            else -> R.drawable.multipart_button_middle_selected
        }
        return ResUtil.getDrawable(context, backgroundId)
    }

    private fun setButtonMargins(button: AppCompatTextView, index: Int) {
        val layoutParams = button.layoutParams as LayoutParams
        layoutParams.topMargin = 9
        layoutParams.bottomMargin = 9
        when (index) {
            1 -> layoutParams.leftMargin = 9
            getExpectedChildCount() -> layoutParams.rightMargin = 9
        }
    }

    private fun changeBackgroundOnClick(clickedView: View) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val background: Drawable? = if (child == clickedView) {
                getButtonBackgroundSelected(i)
            } else {
                getButtonBackgroundUnselected(i)
            }
            child.background = background
        }
    }

    private fun getExpectedChildCount() = buttonValues.size * 2 - 1

    private fun invokeCurrentListener(clickedValue: String) = listener(clickedValue)

    fun setButtonClickListener(listener: (String) -> Unit) {
        this.listener = listener
    }

    fun clickChildAt(position: Int) {
        getChildAt(position).performClick()
    }
}