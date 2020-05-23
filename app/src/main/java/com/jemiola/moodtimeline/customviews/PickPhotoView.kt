package com.jemiola.moodtimeline.customviews

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
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


const val REQUEST_PHOTO = 36

class PickPhotoView : FrameLayout {

    private val selectedPictureImageView: ImageView
    private val editPictureImageView: ImageView
    private var fragment: Fragment? = null
    private var takePhotoFileUri: Uri? = null

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

    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
            intent?.data?.let { uri ->
                onPickPhotoSuccess(uri)
            }
            takePhotoFileUri?.let { uri ->
                onTakePhotoSuccess(uri)
            }
        }
    }

    private fun onPickPhotoSuccess(uri: Uri) {
        val pathToPhoto = ImageUtils.getPathFromUri(uri)
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir != null) {
            val pathToOptimisedFile =
                ImageUtils.saveOptimisedPictureFromPathAndReturnPath(pathToPhoto, storageDir)
            setPathAsSelectedPicture(pathToOptimisedFile)
        }
    }

    private fun onTakePhotoSuccess(uri: Uri) {
        val pathToPhoto = ImageUtils.getPathFromUri(uri)
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir != null) {
            val pathToOptimisedFile =
                ImageUtils.overwriteOptimisedPictureFromPathAndReturnPath(pathToPhoto)
            setPathAsSelectedPicture(pathToOptimisedFile)
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
        takePhotoFileUri = null
        if (!PermissionsUtil.isStoragePermissionGranted() &&
            !PermissionsUtil.isCameraPermissionGranted()
        ) {
            PermissionsUtil.askForStoragePermission(context) {
                PermissionsUtil.askForCameraPermission(context,
                    onDeniedAction = { startActivityGallery() },
                    onGrantedAction = { startActivityGalleryAndCamera() })
            }
        } else if (!PermissionsUtil.isStoragePermissionGranted() &&
            PermissionsUtil.isCameraPermissionGranted()
        ) {
            PermissionsUtil.askForStoragePermission(context) {
                startActivityGalleryAndCamera()
            }
        } else if (PermissionsUtil.isStoragePermissionGranted() &&
            !PermissionsUtil.isCameraPermissionGranted()
        ) {
            PermissionsUtil.askForCameraPermission(context,
                onDeniedAction = { startActivityGallery() },
                onGrantedAction = { startActivityGalleryAndCamera() }
            )
        } else if (PermissionsUtil.isStoragePermissionGranted() &&
            PermissionsUtil.isCameraPermissionGranted()
        ) {
            startActivityGalleryAndCamera()
        }
    }

    private fun startActivityGallery() {
        val photoPickerIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        photoPickerIntent.type = "image/*"
        fragment?.startActivityForResult(photoPickerIntent, REQUEST_PHOTO)
    }

    private fun startActivityGalleryAndCamera() {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir != null) {
            val galleryPhoto =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryPhoto.type = "image/*"
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoFileUri = ImageUtils.createFileAndGetURI(storageDir)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoFileUri)
            val chooser = Intent.createChooser(galleryPhoto, ResUtil.getString(R.string.gallery))
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
            fragment?.startActivityForResult(chooser, REQUEST_PHOTO)
        }
    }
}