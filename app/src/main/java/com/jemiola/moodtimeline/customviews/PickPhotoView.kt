package com.jemiola.moodtimeline.customviews

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.utils.ImageUtils
import com.jemiola.moodtimeline.utils.PermissionsUtil
import com.jemiola.moodtimeline.utils.ResUtil

const val REQUEST_IMAGE_GALLERY = 36

class PickPhotoView : FrameLayout {

    private val selectedPictureImageView: ImageView
    private val editPictureImageView: ImageView
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
        editPictureImageView = findViewById(R.id.editPictureImageView)
        setupPickPhotoOnClick()
    }

    fun setFragment(fragment: Fragment) {
        this.fragment = fragment
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val pathToPhoto = ImageUtils.getPathFromUri(uri)
                val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                if (storageDir != null) {
                    val pathToOptimisedFile =
                        ImageUtils.saveOptimisedPictureAndReturnPath(pathToPhoto, storageDir)
                    setPathAsSelectedPicture(pathToOptimisedFile)
                }
            }
        }
    }

    fun setPathAsSelectedPicture(path: String?) {
        val pictureBitmap = ImageUtils.getBitmapDrawableFromPath(path)
        if (pictureBitmap != null) {
            this.picturePath = path
            editPictureImageView.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_edit))
            selectedPictureImageView.setImageDrawable(pictureBitmap)
        }
    }

    private fun setupPickPhotoOnClick() {
        setOnClickListener { onPickPhotoClick() }
    }

    private fun onPickPhotoClick() {
        if (!PermissionsUtil.isStoragePermissionGranted()) {
            PermissionsUtil.askForStoragePermission(context) { pickPhoto() }
        } else if (PermissionsUtil.isStoragePermissionGranted()) {
            pickPhoto()
        }
    }

    private fun pickPhoto() {
        val photoPickerIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        photoPickerIntent.type = "image/*"
        fragment?.startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY)
    }
}