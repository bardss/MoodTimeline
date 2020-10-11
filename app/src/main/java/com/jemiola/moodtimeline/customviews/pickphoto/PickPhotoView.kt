package com.jemiola.moodtimeline.customviews.pickphoto

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.customviews.pickphoto.PickPhotoViewMode.ENABLE_ADD
import com.jemiola.moodtimeline.customviews.pickphoto.PickPhotoViewMode.ONLY_SHOW
import com.jemiola.moodtimeline.utils.*


const val REQUEST_PHOTO = 36

class PickPhotoView(
    context: Context,
    private val mode: PickPhotoViewMode
) : FrameLayout(context), PickPhotoReceiver {

    private val selectedPictureImageView: ImageView
    private val editPictureImageView: ImageView
    private var takePhotoFileUri: Uri? = null
    private var pickPhotoFragment: PickPhotoFragment? = null
    private var pictureHighlighted = false

    var picturePath: String? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_pick_photo, this)
        selectedPictureImageView = findViewById(R.id.selectedPictureImageView)
        editPictureImageView = findViewById(R.id.editPictureImageView)
        setupPickPhotoOnClick()
        setupView()
    }

    fun setPickPhotoFragment(pickPhotoFragment: PickPhotoFragment?) {
        this.pickPhotoFragment = pickPhotoFragment
    }

    override fun onPicturePicked(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
            intent?.data?.let { uri ->
                onPickPhotoSuccess(uri)
            }
            takePhotoFileUri?.let { uri ->
                onTakePhotoSuccess(uri)
            }
        }
    }

    private fun addNextPickPhotoViewInParent() {
        val parent = parent as PickPhotoLayout
        parent.addPickPhotoView()
    }

    private fun setPickPhotoReceiverInParent() {
        val parent = parent as PickPhotoLayout
        parent.setPickPhotoReceiver(this)
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
            addNextPickPhotoViewInParent()
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
        when (mode) {
            ONLY_SHOW -> onClickWithShowMode()
            ENABLE_ADD -> setupOnPickPhotoClick()
        }
    }

    private fun setupView() {
        when (mode) {
            ONLY_SHOW -> setupOnlyShowView()
            ENABLE_ADD -> setupEnableAddView()
        }
    }

    private fun setupOnPickPhotoClick() {
        setOnClickListener { setupOnPickPhotoClick() }
    }

    private fun setupOnlyShowView() {
        editPictureImageView.visibility = View.GONE
        selectedPictureImageView.alpha = 0.7f
    }

    private fun setupEnableAddView() {
        editPictureImageView.visibility = View.VISIBLE
        selectedPictureImageView.alpha = 0.4f
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onClickWithShowMode() {
        val duration = 250
        setOnTouchListener { view, event ->
            when (event.action) {
                ACTION_DOWN -> onPictureActionDown(duration, view)
                ACTION_UP -> onPictureActionUp(duration, view)
            }
            view.disableFor(duration)
            true
        }
    }

    private fun onPictureActionUp(duration: Int, view: View) {
        if (pictureHighlighted) AnimUtils.animateAlpha(duration, 0.7f, view)
        pictureHighlighted = false
    }

    private fun onPictureActionDown(duration: Int, view: View) {
        if (!pictureHighlighted) AnimUtils.animateAlpha(duration, 1f, view)
        pictureHighlighted = true
        Handler().postDelayed({ onPictureActionUp(duration, view) }, 3000)
    }

    private fun onClickWithAddMode() {
        takePhotoFileUri = null
        setPickPhotoReceiverInParent()
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
        pickPhotoFragment?.startActivityForResult(photoPickerIntent, REQUEST_PHOTO)
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
            pickPhotoFragment?.startActivityForResult(chooser, REQUEST_PHOTO)
        }
    }
}