package com.jemiola.moodtimeline.views.moods

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainMoodsPagerAdapter(parent: Fragment) : FragmentStateAdapter(parent) {

    private val pagerFragments = mutableListOf<Fragment>()

    fun setFragments(fragments: List<Fragment>) {
        pagerFragments.addAll(fragments)
    }

    override fun getItemCount(): Int = pagerFragments.size

    override fun createFragment(position: Int): Fragment = pagerFragments[position]
}