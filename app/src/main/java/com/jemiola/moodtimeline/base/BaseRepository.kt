package com.jemiola.moodtimeline.base

import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import kotlin.coroutines.CoroutineContext

abstract class BaseRepository : KoinComponent, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun cancelCoroutines() {
        coroutineContext.cancelChildren()
    }

    fun <T: Any> launchCallbackRequest(
        request: () -> T,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit) {
        try {
            launch {
                val response = request.invoke()
                launch(Dispatchers.Main) {
                    onSuccess.invoke(response)
                }
            }
        } catch (error: Throwable) {
            cancel()
            onError.invoke(error)
        }
    }

}