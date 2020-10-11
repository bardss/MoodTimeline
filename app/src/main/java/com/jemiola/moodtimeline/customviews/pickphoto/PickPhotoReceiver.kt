package com.jemiola.moodtimeline.customviews.pickphoto

import android.content.Intent

interface PickPhotoReceiver {
    fun onPicturePicked(requestCode: Int, resultCode: Int, intent: Intent?)
}