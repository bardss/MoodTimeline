package com.jemiola.moodtimeline.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

interface BasePresenter: CoroutineScope {

    val job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

}