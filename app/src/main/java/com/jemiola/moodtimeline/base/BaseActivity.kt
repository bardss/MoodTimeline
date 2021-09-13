package com.jemiola.moodtimeline.base

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.koin.core.KoinComponent
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(), KoinComponent {

    abstract val presenter: BasePresenter

    override fun onDestroy() {
        presenter.cancelCoroutines()
        super.onDestroy()
    }
}