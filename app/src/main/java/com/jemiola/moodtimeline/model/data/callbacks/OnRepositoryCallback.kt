package com.jemiola.moodtimeline.model.data.callbacks

interface OnRepositoryCallback<T> {
    fun onSuccess(result: T)
    fun onError()
}