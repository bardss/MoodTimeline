package com.jemiola.moodtimeline.utils.speechtotext

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer

class SpeechRecognitionListener(
    public val handler: SpeechToTextHandler,
    public val outputHandler: SpeechToTextOutput
) : RecognitionListener {

    public var previousTextValue: String = ""
    public var previousPartialResult: String = ""

    override fun onBeginningOfSpeech() {
        previousTextValue = outputHandler.getTextAlreadyInInput()
        if (previousTextValue.isNotEmpty() &&
            !isPreviousCharacterANewLine(previousTextValue)) previousTextValue = "$previousTextValue "
        outputHandler.setMicrophoneIconAsListening()
    }

    override fun onEndOfSpeech() {
        handler.isListening = false
        previousPartialResult = ""
        previousTextValue = ""
        outputHandler.setMicrophoneIconAsNotListening()
    }

    override fun onError(error: Int) {
        if (error != SpeechRecognizer.ERROR_NO_MATCH) {
            handler.isListening = false
            outputHandler.setMicrophoneIconAsNotListening()
        }
    }

    override fun onPartialResults(partialResults: Bundle) {
        val partialResult = "" + partialResults.get("results_recognition")
        if (partialResult.length > 2 && previousPartialResult.length < partialResult.length) {
            var result = partialResult.substring(1, partialResult.length - 1)
            result = replaceWordWhenContains(result, "enter ", "\n")
            result = replaceWordWhenContains(result, "enter", "\n")
            result = replaceWordWhenContains(result, "kropka", ".")
            result = replaceWordWhenContains(result, "dot", ".")
            result = replaceWordWhenContains(result, "przecinek", ",")
            result = replaceWordWhenContains(result, "comma", ",")
            if (previousTextValue.isEmpty() || isLastCharacterADot(result)) {
                result = result.capitalize()
            }
            outputHandler.setTextToInput(previousTextValue + result)
            previousPartialResult = result
        }
    }

    override fun onReadyForSpeech(params: Bundle) {
        outputHandler.setMicrophoneIconAsListening()
    }

    override fun onResults(results: Bundle) {
        handler.isListening = false
        outputHandler.setMicrophoneIconAsNotListening()
    }

    override fun onRmsChanged(rmsdB: Float) {}

    override fun onEvent(eventType: Int, params: Bundle) {}

    override fun onBufferReceived(buffer: ByteArray) {}

    public fun isLastCharacterADot(text: String): Boolean {
        val textLength = text.length
        return ((textLength > 2) && text[textLength - 2] == '.') ||
                ((textLength > 1) && text[textLength - 1] == '.')
    }

    public fun replaceWordWhenContains(text: String, oldValue: String, newValue: String): String {
        return if (text.contains(oldValue)) {
            text.replace(oldValue, newValue)
        } else text
    }

    public fun isPreviousCharacterANewLine(text: String): Boolean {
        val textLength = text.length
        return textLength > 2 && text.substring(textLength - 2, textLength - 1) == "\n"
    }
}