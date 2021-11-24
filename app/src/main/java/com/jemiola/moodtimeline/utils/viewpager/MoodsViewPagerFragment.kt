package com.jemiola.moodtimeline.utils.viewpager

import com.jemiola.moodtimeline.base.BaseFragment

interface ViewPagerParentFragment {
    fun swipeLeft()
    fun swipeRight()
}

abstract class ViewPagerChildFragment : BaseFragment() {

    fun swipeViewPagerLeft() {
        (parentFragment as ViewPagerParentFragment).swipeLeft()
    }

    fun swipeViewPagerRight() {
        (parentFragment as ViewPagerParentFragment).swipeRight()
    }
}