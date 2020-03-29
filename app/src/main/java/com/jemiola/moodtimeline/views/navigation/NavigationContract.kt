package com.jemiola.moodtimeline.views.navigation

import androidx.fragment.app.Fragment

interface NavigationContract {
    interface Presenter
    interface View {
        fun popFragment()
        fun pushFragment(fragment: Fragment)
    }
}