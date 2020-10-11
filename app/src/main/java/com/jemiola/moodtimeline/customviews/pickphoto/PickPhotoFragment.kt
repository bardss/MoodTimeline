package com.jemiola.moodtimeline.customviews.pickphoto

import android.content.Intent

interface PickPhotoFragment {
    fun startActivityForResult(intent: Intent, requestCode: Int)
}