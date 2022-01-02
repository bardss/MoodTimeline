package com.jemiola.moodtimeline.utils.viewpager

import com.jemiola.moodtimeline.base.BaseFragmentMVP

interface ViewPagerParentFragment {
    fun swipeLeft()
    fun swipeRight()
}

abstract class ViewPagerChildFragment : BaseFragmentMVP() {

    fun swipeViewPagerLeft() {
        (parentFragment as ViewPagerParentFragment).swipeLeft()
    }

    fun swipeViewPagerRight() {
        (parentFragment as ViewPagerParentFragment).swipeRight()
    }
}