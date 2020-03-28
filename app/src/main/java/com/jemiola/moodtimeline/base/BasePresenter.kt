package com.jemiola.moodtimeline.base

import com.jemiola.moodtimeline.model.data.callbacks.OnRepositoryCallback
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter(
    open val repository: BaseRepository
): CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job

    fun cancelCoroutines() {
        repository.cancelCoroutines()
        coroutineContext.cancelChildren()
    }

    protected fun <T> createRepositoryCallback(onSuccessAction: (result: T) -> Unit, onErrorAction: () -> Unit): OnRepositoryCallback<T> {
        return object : OnRepositoryCallback<T> {
            override fun onSuccess(result: T) {
                onSuccessAction.invoke(result)
            }

            override fun onError() {
                onErrorAction.invoke()
            }
        }
    }
}