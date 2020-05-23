package com.jemiola.moodtimeline.base

import androidx.appcompat.app.AppCompatActivity
import org.koin.core.KoinComponent

abstract class BaseActivity : AppCompatActivity(), KoinComponent {

    abstract val presenter: BasePresenter

    override fun onDestroy() {
        presenter.cancelCoroutines()
        super.onDestroy()
    }
}