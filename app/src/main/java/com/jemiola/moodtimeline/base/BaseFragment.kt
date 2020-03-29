package com.jemiola.moodtimeline.base

import androidx.fragment.app.Fragment
import com.jemiola.moodtimeline.views.navigation.NavigationActivity
import com.jemiola.moodtimeline.views.navigation.NavigationContract
import org.koin.core.KoinComponent

abstract class BaseFragment : Fragment(), KoinComponent {
    abstract val presenter: BasePresenter

    override fun onDestroy() {
        presenter.cancelCoroutines()
        super.onDestroy()
    }

    fun pushFragment(fragment: Fragment) {
        (activity as? NavigationContract.View)?.pushFragment(fragment)
    }

    fun popFragment() {
        (activity as? NavigationContract.View)?.popFragment()
    }
}