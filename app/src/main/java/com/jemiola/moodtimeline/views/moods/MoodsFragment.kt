package com.jemiola.moodtimeline.views.moods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentMoodsBinding
import com.jemiola.moodtimeline.utils.viewpager.ViewPagerParentFragment
import com.jemiola.moodtimeline.views.moods.calendar.CalendarFragment
import com.jemiola.moodtimeline.views.moods.search.SearchFragment
import com.jemiola.moodtimeline.views.moods.timeline.TimelineFragment
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class MoodsFragment : BaseFragment(), MoodsContract.View, ViewPagerParentFragment {

    override val presenter: MoodsPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentMoodsBinding
    private lateinit var pagerAdapter: MoodsPagerAdapter
    private val pagerFragments = listOf(
        CalendarFragment(),
        TimelineFragment(),
        SearchFragment()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentMoodsBinding.inflate(inflater, container, false)
            setupViewPager()
        }
        return binding.root
    }

    private fun setupViewPager() {
        pagerAdapter = MoodsPagerAdapter(this)
        pagerAdapter.setFragments(pagerFragments)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.setCurrentItem(1, false)
        binding.viewPager.isSaveEnabled = false
    }

    override fun swipeLeft() {
        val currentItem = binding.viewPager.currentItem
        binding.viewPager.setCurrentItem(currentItem - 1, true)
    }

    override fun swipeRight() {
        val currentItem = binding.viewPager.currentItem
        binding.viewPager.setCurrentItem(currentItem + 1, true)
    }
}