package com.jemiola.moodtimeline.base

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.fragment.app.Fragment
import com.jemiola.moodtimeline.views.navigation.NavigationActivity
import com.jemiola.moodtimeline.views.navigation.NavigationContract
import com.jemiola.moodtimeline.views.splash.SplashActivity
import org.koin.core.KoinComponent

abstract class Fragmenciak : Fragment(), KoinComponent {
    abstract val a: BasePresenter

    override fun onDestroy() {
        a.cancelCoroutines()
        super.onDestroy()
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    fun pushFragment(fragment: Fragment) {
        (activity as? NavigationContract.View)?.pushFragment(fragment)
    }

    fun popFragment() {
        (activity as? NavigationContract.View)?.popFragment()
    }

    fun hideBottomMenu() {
        (activity as? NavigationContract.View)?.hideBottomMenu()
    }

    fun showBottomMenu() {
        (activity as? NavigationContract.View)?.showBottomMenu()
    }

    fun startLoading() {
        (activity as? NavigationContract.View)?.startLoading()
    }

    fun stopLoading() {
        (activity as? NavigationContract.View)?.stopLoading()
    }

    fun restartApp() {
        val intentToSplash = Intent(context, SplashActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intentToSplash)
        (activity as? NavigationActivity)?.finish()
    }
}