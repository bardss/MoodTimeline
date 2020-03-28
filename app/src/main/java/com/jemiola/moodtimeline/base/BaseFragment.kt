package com.jemiola.moodtimeline.base

import androidx.fragment.app.Fragment
import org.koin.core.KoinComponent

abstract class BaseFragment : Fragment(), KoinComponent {
    abstract val presenter: BasePresenter

    override fun onDestroy() {
        presenter.cancelCoroutines()
        super.onDestroy()
    }
}