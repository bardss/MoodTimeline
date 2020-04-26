package com.jemiola.moodtimeline.views.timeline

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.customviews.RalewayEditText
import com.jemiola.moodtimeline.databinding.FragmentTimelineBinding
import com.jemiola.moodtimeline.model.data.ExtraKeys
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBO
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.PermissionsUtil
import com.jemiola.moodtimeline.utils.ResUtil
import com.jemiola.moodtimeline.views.calendar.CalendarFragment
import com.jemiola.moodtimeline.views.detailstimelinemood.DetailsTimelineMoodFragment
import com.jemiola.moodtimeline.views.edittimelinemood.EditTimelineMoodFragment
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import java.util.*

class TimelineFragment : BaseFragment(), TimelineContract.View {

    override val presenter: TimelinePresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentTimelineBinding
    private var isSearchOpened = false
    private var isCalendarOpened = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimelineBinding.inflate(inflater, container, false)
        setupStoragePermissions()
        setupTimeline()
        setupSearchView()
        setupCalendarView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        presenter.requestTimelineMoods()
    }

    private fun setupStoragePermissions() {
        if (!PermissionsUtil.isStoragePermissionGranted()) {
            PermissionsUtil.askForStoragePermission()
        }
    }

    private fun setupTimeline() {
        with(binding.timelineRecyclerView) {
            adapter = TimelineAdapter(this@TimelineFragment)
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun setTimelineMoods(moods: List<TimelineMoodBO>) {
        val adapter = binding.timelineRecyclerView.adapter
        (adapter as TimelineAdapter).setTimelineMoods(moods)
    }

    override fun openEditTimelineMoodActivity(mood: TimelineMoodBO) {
        val editTimelineMoodFragment = EditTimelineMoodFragment()
        editTimelineMoodFragment.arguments = createBundleWithTimelineMood(mood)
        pushFragment(editTimelineMoodFragment)
    }

    override fun openTimelineMoodDetails(mood: TimelineMoodBO) {
        val detailsTimelineMoodFragment = DetailsTimelineMoodFragment()
        detailsTimelineMoodFragment.arguments = createBundleWithTimelineMood(mood)
        pushFragment(detailsTimelineMoodFragment)
    }

    private fun createBundleWithTimelineMood(mood: TimelineMoodBO): Bundle {
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
            ResUtil.getColorAsColorStateList(R.color.colorTitle)
        binding.fromEditText.setHintTextColor(ResUtil.getColorAsColorStateList(R.color.colorMoodNone))
        binding.toEditText.backgroundTintList = ResUtil.getColorAsColorStateList(R.color.colorTitle)
        binding.toEditText.setHintTextColor(ResUtil.getColorAsColorStateList(R.color.colorMoodNone))
    }

    private fun onSearchClick() {
        val distance = binding.timelineTopLayout.width
        val searchIconWidth = binding.searchImageView.width
        val timelineLayoutPadding = binding.timelineLayout.paddingStart
        if (!isSearchOpened) {
            isSearchOpened = true
            val hideDistance = -distance + searchIconWidth + timelineLayoutPadding
            AnimUtils.animateMove(500, hideDistance, binding.timelineTopLayout)
            AnimUtils.animateMove(500, 0, binding.searchTopLayout)
        } else {
            isSearchOpened = false
            AnimUtils.animateMove(500, 0, binding.timelineTopLayout)
            AnimUtils.animateMove(500, distance, binding.searchTopLayout) {
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
            val hideDistance = distance - calendarIconWidth - timelineLayoutPadding
            AnimUtils.animateMove(500, hideDistance, binding.timelineTopLayout)
            AnimUtils.animateMove(500, distance, binding.timelineRecyclerView)
            AnimUtils.animateMove(500, 0, binding.calendarTopLayout)
            AnimUtils.animateMove(500, 0, binding.calendarFragmentLayout)
        } else {
            isCalendarOpened = false
            AnimUtils.animateMove(500, 0, binding.timelineTopLayout)
            AnimUtils.animateMove(500, 0, binding.timelineRecyclerView)
            AnimUtils.animateMove(500, - distance, binding.calendarTopLayout)
            AnimUtils.animateMove(500, - distance, binding.calendarFragmentLayout)
        }
    }

    private fun initialSearchTopLayoutMoveOutOfScreen() {
        val distance = binding.timelineTopLayout.width
        AnimUtils.animateMove(500, distance, binding.searchTopLayout) {
            binding.searchTopLayout.visibility = View.VISIBLE
        }
    }

    private fun initialCalendarTopLayoutMoveOutOfScreen() {
        val distance = binding.timelineTopLayout.width
        AnimUtils.animateMove(500, -distance, binding.calendarTopLayout) {
            binding.calendarTopLayout.visibility = View.VISIBLE
        }
        AnimUtils.animateMove(500, -distance, binding.calendarFragmentLayout) {
            binding.calendarFragmentLayout.visibility = View.VISIBLE
        }
    }

    private fun setupSearchCalendars() {
        context?.let { notNullContext ->
            val pickerFrom = createDatePicker(notNullContext, binding.fromEditText)
            binding.fromEditText.setOnClickListener { pickerFrom.show() }
            val pickerTo = createDatePicker(notNullContext, binding.toEditText)
            binding.toEditText.setOnClickListener { pickerTo.show() }
            setupDatePickerBlockades(pickerFrom, pickerTo)
            setupSearchTextWatchers(pickerFrom, pickerTo)
        }

    }

    private fun createOnDatePickedListener(editText: RalewayEditText) =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val dateText = presenter.createDateTextFrom(dayOfMonth, monthOfYear + 1, year)
            editText.setText(dateText)
        }

    private fun createDatePicker(context: Context, editText: RalewayEditText): DatePickerDialog {
        return DatePickerDialog(
            context,
            createOnDatePickedListener(editText),
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun setupSearchTextWatchers(
        fromDatePicker: DatePickerDialog,
        toDatePicker: DatePickerDialog
    ) {
        val afterTextChangedAction = { _: Editable? ->
            val fromText = binding.fromEditText.text
            val toText = binding.toEditText.text
            if (fromText?.isNotEmpty() == true && toText?.isNotEmpty() == true) {
                presenter.requestTimelineMoods()
            }
            setupDatePickerBlockades(fromDatePicker, toDatePicker)
        }
        binding.fromEditText.doAfterTextChanged(afterTextChangedAction)
        binding.toEditText.doAfterTextChanged(afterTextChangedAction)
    }

    private fun setupDatePickerBlockades(
        fromDatePicker: DatePickerDialog,
        toDatePicker: DatePickerDialog
    ) {
        if (binding.toEditText.text?.isNotEmpty() == true) {
            fromDatePicker.datePicker.maxDate = presenter.getSearchToDateLong()
        }
        if (binding.fromEditText.text?.isNotEmpty() == true) {
            toDatePicker.datePicker.minDate = presenter.getSearchFromDateLong()
        }
    }

    override fun getFromDate(): String {
        return binding.fromEditText.text.toString()
    }

    override fun getToDate(): String {
        return binding.toEditText.text.toString()
    }

}