package com.jemiola.moodtimeline.views.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.gridlayout.widget.GridLayout
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.customviews.CalendarDayView
import com.jemiola.moodtimeline.customviews.CalendarMoodDayView
import com.jemiola.moodtimeline.databinding.FragmentCalendarBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.OnSwipeListener
import com.jemiola.moodtimeline.utils.ResUtil
import com.jemiola.moodtimeline.utils.disableFor
import com.jemiola.moodtimeline.views.detailstimelinemood.DetailsTimelineMoodFragment
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class CalendarFragment : BaseFragment(), CalendarContract.View {

    override val presenter: CalendarPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::binding.isInitialized) {
            binding = FragmentCalendarBinding.inflate(inflater, container, false)
        }
        return binding.root
    }

    override fun onStart() {
        setupCalendarView()
        super.onStart()
    }

    private fun setupCalendarView() {
        presenter.setupCalendar()
        setupMonthChange()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupMonthChange() {
        val disableTime = 1000
        binding.arrowLeftImageView.setOnClickListener {
            it.disableFor(disableTime)
            presenter.openPreviousMonth()
        }
        binding.arrowRightImageView.setOnClickListener {
            it.disableFor(disableTime)
            presenter.openNextMonth()
        }
        binding.calendarTopBarGridLayout.setOnTouchListener(createMonthSwipeListener(disableTime))
        binding.calendarDaysGridLayout.setOnTouchListener(createMonthSwipeListener(disableTime))
    }

    private fun createMonthSwipeListener(disableTime: Int): OnSwipeListener {
        return object : OnSwipeListener(context) {
            override fun onSwipeRight() {
                binding.calendarTopBarGridLayout.disableFor(disableTime)
                presenter.openPreviousMonth()
            }

            override fun onSwipeLeft() {
                binding.calendarTopBarGridLayout.disableFor(disableTime)
                presenter.openNextMonth()
            }
        }
    }

    override fun setMonthName(monthText: String) {
        binding.monthTextView.text = monthText
    }

    override fun addNotCurrentMonthDay(day: Int) {
        context?.let { notNullContext ->
            val dayView = createDayView(notNullContext)
            binding.calendarDaysGridLayout.addView(dayView)
            dayView.day = day
            dayView.dayTextView.setTextColor(ResUtil.getColor(context, R.color.colorMoodNone))
            dayView.layoutParams = createCalendarDayLayoutParams()
        }
    }

    override fun addCurrentMonthDefaultDay(day: Int) {
        context?.let { notNullContext ->
            val dayView = createDayView(notNullContext)
            binding.calendarDaysGridLayout.addView(dayView)
            dayView.day = day
            dayView.dayTextView.setTextColor(ResUtil.getColor(context, R.color.colorTitle))
            dayView.layoutParams = createCalendarDayLayoutParams()
        }
    }

    override fun addCurrentMonthMoodDay(day: Int, mood: TimelineMoodBOv2) {
        context?.let { notNullContext ->
            val moodDayView = createMoodDayView(notNullContext)
            binding.calendarDaysGridLayout.addView(moodDayView)
            moodDayView.day = day
            moodDayView.mood = mood.circleMood
            moodDayView.layoutParams = createCalendarDayLayoutParams()
            moodDayView.setOnClickListener { openTimelineMoodDetails(mood) }
        }
    }

    private fun createCalendarDayLayoutParams(): GridLayout.LayoutParams {
        val dayLayoutParams = GridLayout.LayoutParams(ViewGroup.LayoutParams(0, WRAP_CONTENT))
        dayLayoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        return dayLayoutParams
    }

    private fun createDayView(context: Context): CalendarDayView {
        return CalendarDayView(context)
    }

    private fun createMoodDayView(context: Context): CalendarMoodDayView {
        return CalendarMoodDayView(context)
    }

    override fun clearDaysInCalendar() {
        binding.calendarDaysGridLayout.removeAllViews()
    }

    override fun hideCalendar(doOnAnimationFinished: () -> Unit) {
        AnimUtils.fadeOut(50, binding.monthTextView)
        AnimUtils.fadeOut(50, doOnAnimationFinished, binding.calendarDaysGridLayout)
    }

    override fun showCalendar() {
        AnimUtils.fadeIn(50, binding.monthTextView)
        AnimUtils.fadeIn(50, binding.calendarDaysGridLayout)
    }

    private fun openTimelineMoodDetails(mood: TimelineMoodBOv2) {
        val detailsTimelineMoodFragment = DetailsTimelineMoodFragment()
        detailsTimelineMoodFragment.arguments = createBundleWithTimelineMood(mood)
        pushFragment(detailsTimelineMoodFragment)
    }

    private fun createBundleWithTimelineMood(mood: TimelineMoodBOv2): Bundle {
        return Bundle().apply {
            putSerializable(ExtraKeys.TIMELINE_MOOD, mood)
        }
    }

    override fun requestCalendarLayout() {
//        binding.calendarDaysGridLayout.requestLayout()
    }
}