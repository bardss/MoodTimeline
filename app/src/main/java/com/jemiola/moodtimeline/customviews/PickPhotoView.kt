package com.jemiola.moodtimeline.customviews

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.utils.ImageUtils

const val REQUEST_IMAGE_GALLERY = 36
const val PICTURE_QUALITY = 60

class PickPhotoView : FrameLayout {

    private val selectedPictureImageView: ImageView
    private var fragment: Fragment? = null

    var picturePath: String? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.view_pick_photo, this)
        selectedPictureImageView = findViewById(R.id.selectedPictureImageView)
        setupPickPhotoOnClick()
    }

    fun setFragment(fragment: Fragment) {
        this.fragment = fragment
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val pathToPhoto = ImageUtils.getPathFromUri(uri)
                setPathAsSelectedPicture(pathToPhoto)
            }
        }
    }

    fun setPathAsSelectedPicture(path: String?) {
        val pictureBitmap = ImageUtils.getBitmapDrawableFromPath(path, PICTURE_QUALITY, true)
        if (pictureBitmap != null) {
            this.picturePath = path
            selectedPictureImageView.setImageDrawable(pictureBitmap)
        }
    }

    private fun setupPickPhotoOnClick() {
        setOnClickListener {
            val photoPickerIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            photoPickerIntent.type = "image/*"
            fragment?.startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY)
        }
    }
}