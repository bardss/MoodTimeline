package com.jemiola.moodtimeline.utils.speechtotext

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.jemiola.moodtimeline.utils.PermissionsUtil

class SpeechToTextHandler(
    context: Context?,
    outputHandler: SpeechToTextOutput
) {

    public var speechRecognizer: SpeechRecognizer =
        SpeechRecognizer.createSpeechRecognizer(context)
    public var speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    var isListening: Boolean = false

    init {
        speechRecognizerIntent.let {
            it.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            it.putExtra(
                RecognizerIntent.EXTRA_CALLING_PACKAGE,
                context?.packageName
            )
            it.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
        val speechRecognitionListener = SpeechRecognitionListener(this, outputHandler)
        speechRecognizer.setRecognitionListener(speechRecognitionListener)
    }

    fun onClickMicrophoneIcon(context: Context?) {
        if (PermissionsUtil.isMicrophonePermissionGranted()) {
            onStartListening()
        } else {
            PermissionsUtil.askForMicrophonePermission(
                context,
                onGrantedAction = { onClickMicrophoneIcon(context) }
            )
        }
    }

    public fun onStartListening() {
        if (!isListening) {
            speechRecognizer.startListening(speechRecognizerIntent)
        } else {
            speechRecognizer.stopListening()
        }
        isListening = !isListening
    }
}