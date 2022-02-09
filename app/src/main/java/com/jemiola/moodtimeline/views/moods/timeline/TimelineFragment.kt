package com.jemiola.moodtimeline.views.moods.timeline

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.databinding.FragmentTimelineBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.PaginationScrollListener
import com.jemiola.moodtimeline.utils.viewpager.ViewPagerChildFragment
import com.jemiola.moodtimeline.views.editmood.EditMoodFragment
import com.jemiola.moodtimeline.views.mooddetails.MoodDetailsFragment
import com.jemiola.moodtimeline.views.moods.list.MoodClickActions
import com.jemiola.moodtimeline.views.moods.list.MoodsAdapter
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

const val EMPTY_VIEW_ANIM_DURATION = 100

class TimelineFragment : ViewPagerChildFragment(), TimelineContract.View, MoodClickActions {

    override val presenter: TimelinePresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentTimelineBinding
    private lateinit var paginationScrollListener: PaginationScrollListener
    private var counterComeBackLaterInflater = 0
    private var moodsAdapter: MoodsAdapter
        get() = binding.timelineRecyclerView.adapter as MoodsAdapter
        set(value) {
            binding.timelineRecyclerView.adapter = value
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentTimelineBinding.inflate(inflater, container, false)
            setupTimeline(binding.timelineRecyclerView)
            setupViewPagerNavigation()
            presenter.setupTimetableMoods()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        counterComeBackLaterInflater = 0
        presenter.updateTodaysMood()
    }

    private fun setupTimeline(recyclerView: RecyclerView) {
        with(recyclerView) {
            adapter = MoodsAdapter(this@TimelineFragment)
            val recyclerViewManager = LinearLayoutManager(context)
            layoutManager = recyclerViewManager
            layoutAnimation =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
            scheduleLayoutAnimation()
            paginationScrollListener = createPaginationScrollListener(recyclerViewManager)
            addOnScrollListener(paginationScrollListener)
        }
    }

    override fun updateTodaysMood(mood: TimelineMoodBOv2) {
        moodsAdapter.updateMood(mood)
    }

    override fun setPagedTimelineMoods(moods: List<TimelineMoodBOv2>) {
        if (moods.isEmpty()) paginationScrollListener.isLastPage = true
        paginationScrollListener.isLoadingPage = false
        moodsAdapter.addNextPage(moods)
    }

    override fun setTimelineMoods(moods: List<TimelineMoodBOv2>) {
        moodsAdapter.setTimelineMoods(moods)
    }

    override fun openEditTimelineMoodActivity(mood: TimelineMoodBOv2, isAddingFirstMood: Boolean) {
        val editTimelineMoodFragment = EditMoodFragment()
        editTimelineMoodFragment.arguments = createBundleEditTimelineMood(mood, isAddingFirstMood)
        pushFragment(editTimelineMoodFragment)
    }

    override fun openTimelineMoodDetails(mood: TimelineMoodBOv2) {
        val detailsTimelineMoodFragment = MoodDetailsFragment()
        detailsTimelineMoodFragment.arguments = createBundleDetailsTimelineMood(mood)
        pushFragment(detailsTimelineMoodFragment)
    }

    private fun createBundleEditTimelineMood(
        mood: TimelineMoodBOv2,
        isAddingFirstMood: Boolean
    ): Bundle {
        return Bundle().apply {
            putSerializable(ExtraKeys.TIMELINE_MOOD, mood)
            putBoolean(ExtraKeys.IS_ADD_MOOD_ONBOARDING, isAddingFirstMood)
        }
    }

    private fun createBundleDetailsTimelineMood(mood: TimelineMoodBOv2): Bundle {
        return Bundle().apply {
            putSerializable(ExtraKeys.TIMELINE_MOOD, mood)
        }
    }

    private fun setupViewPagerNavigation() {
        binding.timelineTopLayout.post {
            binding.searchImageView.setOnClickListener { swipeViewPagerRight() }
            binding.calendarImageView.setOnClickListener { swipeViewPagerLeft() }
        }
    }

    override fun showAddEmptyView() {
        setupAddEmptyViewMoodCircle()
        setupAddEmptyViewVisibility()
        setupAddEmptyViewOnClick()
    }

    override fun setupComeBackLaterView() {
        binding.timelineRecyclerView.post {
            val timelineContentLayoutHeight = binding.timelineContentLayout.height
            val timelineListHeight = binding.timelineRecyclerView.height
            val comeBackLaterViewHeight = binding.comeBackLaterLayout.height
            if (timelineListHeight < 100 && counterComeBackLaterInflater < 5) {
                counterComeBackLaterInflater += 1
                Handler(Looper.getMainLooper())
                    .postDelayed({ setupComeBackLaterView() }, 1000)
            } else {
                val contentSum = timelineListHeight + comeBackLaterViewHeight
                if (timelineContentLayoutHeight > contentSum) {
                    AnimUtils.fadeIn(EMPTY_VIEW_ANIM_DURATION, binding.comeBackLaterLayout)
                } else if (binding.comeBackLaterLayout.visibility != View.INVISIBLE) {
                    AnimUtils.fadeOut(EMPTY_VIEW_ANIM_DURATION, binding.comeBackLaterLayout)
                }
            }
        }
    }

    private fun setupAddEmptyViewOnClick() {
        binding.addEmptyViewLayout.setOnClickListener {
            openEditTimelineMoodActivity(presenter.createAddTimelineMood(), true)
        }
    }

    private fun setupAddEmptyViewVisibility() {
        hideBottomMenu()
        binding.calendarImageView.visibility = View.INVISIBLE
        binding.searchImageView.visibility = View.INVISIBLE
        AnimUtils.fadeIn(EMPTY_VIEW_ANIM_DURATION, binding.timelineRecyclerView)
        AnimUtils.fadeIn(EMPTY_VIEW_ANIM_DURATION, binding.addEmptyViewLayout)
    }

    private fun setupAddEmptyViewMoodCircle() {
        binding.addEmptyViewCircle.mood = CircleMoodBO.VERY_GOOD
        binding.addEmptyViewCircle.state = CircleStateBO.ADD
    }

    private fun createPaginationScrollListener(layoutManager: LinearLayoutManager) =
        object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems(pageIndex: Int, pageSize: Int) {
                isLoadingPage = true
                presenter.requestTimetableMoodsPaged(pageIndex, pageSize)
            }
        }

    override fun showTimelineRecyclerView() {
        AnimUtils.fadeIn(EMPTY_VIEW_ANIM_DURATION, binding.timelineRecyclerView)
        AnimUtils.fadeOut(
            EMPTY_VIEW_ANIM_DURATION,
            { binding.addEmptyViewLayout.visibility = View.GONE },
            binding.addEmptyViewLayout
        )
    }
}