package com.jemiola.moodtimeline.base

import androidx.appcompat.app.AppCompatActivity
import org.koin.core.component.KoinComponent

abstract class BaseActivity : AppCompatActivity(), KoinComponent {

    abstract val presenter: BasePresenterMVP

    override fun onDestroy() {
        presenter.cancelCoroutines()
        super.onDestroy()
    }
}