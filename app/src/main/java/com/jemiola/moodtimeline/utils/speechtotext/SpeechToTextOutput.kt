package com.jemiola.moodtimeline.utils.speechtotext

interface SpeechToTextOutput {
    fun setMicrophoneIconAsNotListening()
    fun setMicrophoneIconAsListening()
    fun setTextToInput(text: String)
    fun getTextAlreadyInInput(): String
}