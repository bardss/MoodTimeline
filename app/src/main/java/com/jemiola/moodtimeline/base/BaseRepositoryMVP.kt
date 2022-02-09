package com.jemiola.moodtimeline.base

import com.jemiola.moodtimeline.exceptions.NullResponseException
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import kotlin.coroutines.CoroutineContext

abstract class BaseRepositoryMVP : KoinComponent, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun cancelCoroutines() {
        coroutineContext.cancelChildren()
    }

    fun <T : Any> launchCallbackRequest(
        request: () -> T?,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            launch {
                val response = request.invoke()
                if (response != null) {
                    launch(Dispatchers.Main) {
                        onSuccess.invoke(response)
                    }
                } else {
                    cancel()
                    onError.invoke(NullResponseException())
                }
            }
        } catch (error: Throwable) {
            cancel()
            onError.invoke(error)
        }
    }
}