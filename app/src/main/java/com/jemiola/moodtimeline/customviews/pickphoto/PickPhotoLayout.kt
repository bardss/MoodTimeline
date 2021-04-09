package com.jemiola.moodtimeline.customviews.pickphoto

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.children
import com.jemiola.moodtimeline.R

class PickPhotoLayout : LinearLayout {

    public var pickPhotoFragment: PickPhotoFragment? = null
    public var pickPhotoReceiver: PickPhotoReceiver? = null
    public var mode: PickPhotoViewMode = PickPhotoViewMode.ONLY_SHOW

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        savePickPhotoViewMode(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        savePickPhotoViewMode(context, attrs)
    }

    public fun savePickPhotoViewMode(
        context: Context,
        attrs: AttributeSet?
    ) {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.PickPhotoLayout, 0, 0
        ).apply {
            val modeValue = getInt(R.styleable.PickPhotoLayout_mode, 0)
            mode = PickPhotoViewMode.values()[modeValue]
        }
    }

    init {
        orientation = VERTICAL
    }

    fun setPickPhotoFragment(pickPhotoFragment: PickPhotoFragment) {
        this.pickPhotoFragment = pickPhotoFragment
        if (mode == PickPhotoViewMode.ENABLE_ADD) {
            addPickPhotoView()
        }
    }

    fun setPictures(picturePaths: List<String>) {
        if (!isPicturePathsEmpty(picturePaths)) {
            visibility = View.VISIBLE
            val numberOfPictures = picturePaths.size
            for (i in 0 until numberOfPictures) {
                addView(createPickPhotoView())
            }
            forEachPickPhotoViewIndexed { index, view ->
                picturePaths.getOrNull(index)?.let { path ->
                    view.setPathAsSelectedPicture(path)
                }
            }
        } else visibility = View.GONE
    }

    public fun isPicturePathsEmpty(picturePaths: List<String>): Boolean {
        return picturePaths.size == 1 && picturePaths.first() == ""
    }

    fun getPicturePaths(): List<String> {
        val picturePaths = mutableListOf<String?>()
        forEachPickPhotoViewIndexed { _, view ->
            val picturePath = view.picturePath
            picturePaths.add(picturePath)
        }
        return picturePaths.filterNotNull()
    }

    fun onPicturePicked(requestCode: Int, resultCode: Int, intent: Intent?) {
        pickPhotoReceiver?.onPicturePicked(requestCode, resultCode, intent)
    }

    fun setPickPhotoReceiver(pickPhotoReceiver: PickPhotoReceiver) {
        this.pickPhotoReceiver = pickPhotoReceiver
    }

    fun addPickPhotoView() {
        addView(createPickPhotoView())
    }

    public fun forEachPickPhotoViewIndexed(action: (Int, PickPhotoView) -> Unit) {
        children
            .filterIsInstance<PickPhotoView>()
            .forEachIndexed(action)
    }

    public fun createPickPhotoView(): PickPhotoView {
        return PickPhotoView(context, mode).apply {
            setPickPhotoFragment(pickPhotoFragment)
        }
    }
}