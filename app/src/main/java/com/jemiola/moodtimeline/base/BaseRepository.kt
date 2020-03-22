package com.jemiola.moodtimeline.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent

abstract class BaseRepository : KoinComponent, CoroutineScope by CoroutineScope(Dispatchers.IO)