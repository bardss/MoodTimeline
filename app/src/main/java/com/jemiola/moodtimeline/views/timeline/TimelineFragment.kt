package com.jemiola.moodtimeline.views.timeline

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentTimelineBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.model.data.local.CircleStateBO
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.ResUtil
import com.jemiola.moodtimeline.utils.rangepickers.RangePickersUtil
import com.jemiola.moodtimeline.views.calendar.CalendarFragment
import com.jemiola.moodtimeline.views.detailstimelinemood.DetailsTimelineMoodFragment
import com.jemiola.moodtimeline.views.edittimelinemood.EditTimelineMoodFragment
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

const val MOVE_ANIM_DURATION = 500
const val EMPTY_VIEW_ANIM_DURATION = 100

class TimelineFragment : BaseFragment(), TimelineContract.View {

    override val presenter: TimelinePresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentTimelineBinding
    private var isSearchOpened = false
    private var isCalendarOpened = false
    private var counterComeBackLaterInflater = 0
    private val rangePickersUtil = RangePickersUtil()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::binding.isInitialized) {
            binding = FragmentTimelineBinding.inflate(inflater, container, false)
            setupTimeline()
            setupSearchView()
            setupCalendarView()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        counterComeBackLaterInflater = 0
        presenter.setupTimetableMoods()
    }

    private fun setupTimeline() {
        with(binding.timelineRecyclerView) {
            adapter = TimelineAdapter(this@TimelineFragment)
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun setTimelineMoods(moods: List<TimelineMoodBOv2>) {
        val adapter = binding.timelineRecyclerView.adapter
        val animationId =
            if (adapter?.itemCount == 0) R.anim.layout_animation_fade_in
            else R.anim.layout_animation_fall_down
        val animation = AnimationUtils.loadLayoutAnimation(context, animationId)
        (adapter as TimelineAdapter).setTimelineMoods(moods)
        binding.timelineRecyclerView.layoutAnimation = animation
        binding.timelineRecyclerView.scheduleLayoutAnimation()
    }

    override fun openEditTimelineMoodActivity(mood: TimelineMoodBOv2, isAddingFirstMood: Boolean) {
        val editTimelineMoodFragment = EditTimelineMoodFragment()
        editTimelineMoodFragment.arguments = createBundleEditTimelineMood(mood, isAddingFirstMood)
        pushFragment(editTimelineMoodFragment)
    }

    override fun openTimelineMoodDetails(mood: TimelineMoodBOv2) {
        val detailsTimelineMoodFragment = DetailsTimelineMoodFragment()
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

    private fun setupSearchView() {
        setupSearchDefaultValues()
        setupSearchEditTextColors()
        setupSearchCalendars()
        binding.timelineTopLayout.post {
            initialSearchTopLayoutMoveOutOfScreen()
            binding.searchImageView.setOnClickListener { onSearchClick() }
        }
    }

    private fun setupSearchCalendars() {
        context?.let {
            val fromEditText = binding.fromEditText
            val toEditText = binding.toEditText
            rangePickersUtil.setupRangeCalendars(
                it,
                fromEditText,
                toEditText
            ) { presenter.searchTimelineMoods() }
        }
    }

    private fun setupCalendarView() {
        setupCalendarFragment()
        binding.timelineTopLayout.post {
            initialCalendarTopLayoutMoveOutOfScreen()
            binding.calendarImageView.setOnClickListener { onCalendarClick() }
        }
    }

    private fun setupCalendarFragment() {
        val calendarFragment = CalendarFragment()
        childFragmentManager
            .beginTransaction()
            .add(R.id.calendarFragmentLayout, calendarFragment)
            .commit()
    }

    private fun setupSearchDefaultValues() {
        val fromDate = presenter.getDefaultFromDate()
        val toDate = presenter.getDefaultToDate()
        binding.fromEditText.setText(fromDate)
        binding.toEditText.setText(toDate)
    }

    private fun setupSearchEditTextColors() {
        binding.fromEditText.backgroundTintList =
            ResUtil.getColorAsColorStateList(resources, R.color.colorTitle)
        binding.fromEditText.setHintTextColor(
            ResUtil.getColorAsColorStateList(
                resources,
                R.color.colorMoodNone
            )
        )
        binding.toEditText.backgroundTintList =
            ResUtil.getColorAsColorStateList(resources, R.color.colorTitle)
        binding.toEditText.setHintTextColor(
            ResUtil.getColorAsColorStateList(
                resources,
                R.color.colorMoodNone
            )
        )
    }

    private fun onSearchClick() {
        val distance = binding.timelineTopLayout.width
        val searchIconWidth = binding.searchImageView.width
        val timelineLayoutPadding = binding.timelineLayout.paddingStart
        if (!isSearchOpened) {
            isSearchOpened = true
            animateIconChangeTo(
                binding.searchImageView,
                ResUtil.getDrawable(context, R.drawable.ic_close)
            )
            val hideDistance = -distance + searchIconWidth + timelineLayoutPadding
            AnimUtils.animateMove(MOVE_ANIM_DURATION, hideDistance, binding.timelineTopLayout)
            AnimUtils.animateMove(MOVE_ANIM_DURATION, 0, binding.searchTopLayout)
        } else {
            isSearchOpened = false
            animateIconChangeTo(
                binding.searchImageView,
                ResUtil.getDrawable(context, R.drawable.ic_search)
            )
            AnimUtils.animateMove(MOVE_ANIM_DURATION, 0, binding.timelineTopLayout)
            AnimUtils.animateMove(MOVE_ANIM_DURATION, distance, binding.searchTopLayout) {
                setupSearchDefaultValues()
            }
        }
    }

    private fun onCalendarClick() {
        val distance = binding.timelineTopLayout.width
        val calendarIconWidth = binding.calendarImageView.width
        val timelineLayoutPadding = binding.timelineLayout.paddingStart
        if (!isCalendarOpened) {
            isCalendarOpened = true
            animateIconChangeTo(
                binding.calendarImageView,
                ResUtil.getDrawable(context, R.drawable.ic_close)
            )
            val hideDistance = distance - calendarIconWidth - timelineLayoutPadding
            AnimUtils.animateMove(MOVE_ANIM_DURATION, hideDistance, binding.timelineTopLayout)
            AnimUtils.animateMove(MOVE_ANIM_DURATION, distance, binding.timelineRecyclerView)
            AnimUtils.animateMove(MOVE_ANIM_DURATION, distance, binding.comeBackLaterLayout)
            AnimUtils.animateMove(MOVE_ANIM_DURATION, 0, binding.calendarTopLayout)
            AnimUtils.animateMove(MOVE_ANIM_DURATION, 0, binding.calendarFragmentLayout)
        } else {
            isCalendarOpened = false
            animateIconChangeTo(
                binding.calendarImageView,
                ResUtil.getDrawable(context, R.drawable.ic_calendar)
            )
            AnimUtils.animateMove(MOVE_ANIM_DURATION, 0, binding.timelineTopLayout)
            AnimUtils.animateMove(MOVE_ANIM_DURATION, 0, binding.timelineRecyclerView)
            AnimUtils.animateMove(MOVE_ANIM_DURATION, 0, binding.comeBackLaterLayout)
            AnimUtils.animateMove(MOVE_ANIM_DURATION, -distance, binding.calendarTopLayout)
            AnimUtils.animateMove(MOVE_ANIM_DURATION, -distance, binding.calendarFragmentLayout)
        }
    }

    private fun animateIconChangeTo(iconImageView: ImageView, drawable: Drawable?) {
        AnimUtils.fadeOut(100, {
            iconImageView.setImageDrawable(drawable)
            AnimUtils.fadeIn(100, iconImageView)
        }, iconImageView)
    }


    private fun initialSearchTopLayoutMoveOutOfScreen() {
        val distance = binding.timelineTopLayout.width
        AnimUtils.animateMove(MOVE_ANIM_DURATION, distance, binding.searchTopLayout) {
            binding.searchTopLayout.visibility = View.VISIBLE
        }
    }

    private fun initialCalendarTopLayoutMoveOutOfScreen() {
        val distance = binding.timelineTopLayout.width
        AnimUtils.animateMove(MOVE_ANIM_DURATION, -distance, binding.calendarTopLayout) {
            binding.calendarTopLayout.visibility = View.VISIBLE
        }
        AnimUtils.animateMove(MOVE_ANIM_DURATION, -distance, binding.calendarFragmentLayout) {
            binding.calendarFragmentLayout.visibility = View.VISIBLE
        }
    }

    override fun getFromDate(): String {
        return binding.fromEditText.text.toString()
    }

    override fun getToDate(): String {
        return binding.toEditText.text.toString()
    }

    override fun showSearchEmptyView() {
        hideViewWhenVisible(binding.timelineRecyclerView)
        showViewWhenHidden(binding.searchEmptyViewLayout)

    }

    override fun showTimelineRecyclerView() {
        showViewWhenHidden(binding.timelineRecyclerView)
        showViewWhenHidden(binding.calendarImageView)
        showViewWhenHidden(binding.searchImageView)
        hideViewWhenVisible(binding.searchEmptyViewLayout)
        hideViewWhenVisible(binding.addEmptyViewLayout)
    }

    private fun showViewWhenHidden(view: View) {
        if (view.visibility == View.GONE || view.visibility == View.INVISIBLE) {
            AnimUtils.fadeIn(EMPTY_VIEW_ANIM_DURATION, { view.visibility = View.VISIBLE }, view)
        }
    }

    private fun hideViewWhenVisible(view: View) {
        if (view.visibility != View.GONE) {
            AnimUtils.fadeOut(EMPTY_VIEW_ANIM_DURATION, { view.visibility = View.GONE }, view)
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
                Handler().postDelayed({ setupComeBackLaterView() }, 1000)
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

}