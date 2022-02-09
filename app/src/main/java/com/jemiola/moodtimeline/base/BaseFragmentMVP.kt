package com.jemiola.moodtimeline.base

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.fragment.app.Fragment
import com.jemiola.moodtimeline.views.navigation.NavigationActivity
import com.jemiola.moodtimeline.views.navigation.NavigationContract
import com.jemiola.moodtimeline.views.splash.SplashActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.koin.core.component.KoinComponent
import kotlin.coroutines.CoroutineContext

abstract class BaseFragmentMVP : BaseFragment(), KoinComponent, CoroutineScope {
    abstract val presenter: BasePresenterMVP

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job

    override fun onDestroy() {
        presenter.cancelCoroutines()
        super.onDestroy()
    }
}