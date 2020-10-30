package com.jemiola.moodtimeline.base

import androidx.fragment.app.Fragment
import com.jemiola.moodtimeline.views.navigation.NavigationContract
import org.koin.core.KoinComponent

abstract class BaseFragment : Fragment(), KoinComponent {
    abstract val presenter: BasePresenter

    override fun onDestroy() {
        presenter.cancelCoroutines()
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
}