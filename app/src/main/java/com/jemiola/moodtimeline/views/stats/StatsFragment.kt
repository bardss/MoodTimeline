package com.jemiola.moodtimeline.views.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.customviews.Typefaces
import com.jemiola.moodtimeline.databinding.FragmentStatsBinding
import com.jemiola.moodtimeline.model.data.local.MoodsCountByType
import com.jemiola.moodtimeline.utils.DateFormatterUtil
import com.jemiola.moodtimeline.utils.LocaleUtil
import com.jemiola.moodtimeline.utils.ResUtil
import com.jemiola.moodtimeline.utils.rangepickers.RangePickersUtil
import com.jemiola.moodtimeline.views.stats.StatsRange.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate


class StatsFragment : BaseFragment() {

    private lateinit var binding: FragmentStatsBinding
    private val viewModel by viewModel<StatsViewModel>()
    private val dateFormatter = DateFormatterUtil()
    private val rangePickersUtil = RangePickersUtil()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentStatsBinding.inflate(inflater, container, false)
            setupPieChart()
            setupRangeMultipartButton()
            setupRangeEditTexts()
            setupRangeOnTextChange()
            observeChartRange()
            binding.rangeMultipartButton.clickChildAt(0)
        }
        setupMoodsObserver()
        return binding.root
    }

    private fun setupMoodsObserver() {
        viewModel.moodsCountByType.observe(viewLifecycleOwner) { moodsCountByType ->
            setPieChartData(moodsCountByType)
        }
    }

    private fun setupRangeMultipartButton() {
        binding.rangeMultipartButton.setButtonClickListener {
            val timeNow = LocalDate.now()
            when (it) {
                ResUtil.getString(resources, R.string.all) ->
                    viewModel.setChartRange(ALL, timeNow)
                ResUtil.getString(resources, R.string.year) ->
                    viewModel.setChartRange(YEAR, timeNow)
                ResUtil.getString(resources, R.string.month) ->
                    viewModel.setChartRange(MONTH, timeNow)
                ResUtil.getString(resources, R.string.week) ->
                    viewModel.setChartRange(WEEK, timeNow)
            }
        }
    }

    private fun observeChartRange() {
        viewModel.pieChartRange.observe(viewLifecycleOwner) { pieChartRange ->
            val locale = LocaleUtil.getSystemLocale(requireContext())
            val dateFromFormatted = dateFormatter.getFormattedDate(locale, pieChartRange.from)
            val dateToFormatted = dateFormatter.getFormattedDate(locale, pieChartRange.to)
            binding.fromEditText.setText(dateFromFormatted)
            binding.toEditText.setText(dateToFormatted)
            viewModel.onPieChartRangeChange()
        }
    }

    private fun setupPieChart() {
        with(binding.chartView) {
            isHighlightPerTapEnabled = true
            isRotationEnabled = false
            description.isEnabled = false
            invalidate()
        }
    }

    private fun setPieChartData(moodsCountByType: MoodsCountByType) {
        val entries = mutableListOf<PieEntry>()
        val colors = mutableListOf<Int>()
        if (moodsCountByType.veryGood != 0) {
            entries.add(PieEntry(moodsCountByType.veryGood.toFloat()))
            colors.add(ResUtil.getColor(context, R.color.colorMoodVeryGood))
        }
        if (moodsCountByType.good != 0) {
            entries.add(PieEntry(moodsCountByType.good.toFloat()))
            colors.add(ResUtil.getColor(context, R.color.colorMoodGood))
        }
        if (moodsCountByType.mediocre != 0) {
            entries.add(PieEntry(moodsCountByType.mediocre.toFloat()))
            colors.add(ResUtil.getColor(context, R.color.colorMoodMediocre))
        }
        if (moodsCountByType.bad != 0) {
            entries.add(PieEntry(moodsCountByType.bad.toFloat()))
            colors.add(ResUtil.getColor(context, R.color.colorMoodBad))
        }
        if (moodsCountByType.veryBad != 0) {
            entries.add(PieEntry(moodsCountByType.veryBad.toFloat()))
            colors.add(ResUtil.getColor(context, R.color.colorMoodVeryBad))
        }
        val dataSet = PieDataSet(entries, ResUtil.getString(resources, R.string.moods))
        dataSet.colors = colors
        val data = PieData(dataSet)
        prepareDataToDraw(data, moodsCountByType.getSum())
        binding.chartView.data = data
        binding.chartView.invalidate()
    }

    private fun prepareDataToDraw(data: PieData, allValuesSum: Int) {
        data.setDrawValues(true)
        data.setValueFormatter(PieDataFormatter(allValuesSum))
        data.setValueTextColor(ResUtil.getColor(requireContext(), R.color.colorTextWhite))
        val textSize = ResUtil.getDimenDp(resources, R.dimen.text_size_medium).toFloat()
        data.setValueTextSize(textSize)
        data.setValueTypeface(ResUtil.getTypeface(Typefaces.COMFORTAA_BOLD))
    }

    private fun setupRangeEditTexts() {
        val fromEditText = binding.fromEditText
        val toEditText = binding.toEditText
        rangePickersUtil.setupRangeCalendars(
            requireContext(),
            fromEditText,
            toEditText,
            onEditTextClick = { binding.rangeMultipartButton.unselectAll() }
        )
    }

    private fun setupRangeOnTextChange() {
        val locale = LocaleUtil.getSystemLocale(requireContext())
        binding.fromEditText.doAfterTextChanged { text ->
            val dateFrom = dateFormatter.getDateFromFormattedString(locale, text.toString())
            viewModel.setPieChartRangeDates(fromDate = dateFrom)
        }
        binding.toEditText.doAfterTextChanged { text ->
            val dateTo = dateFormatter.getDateFromFormattedString(locale, text.toString())
            viewModel.setPieChartRangeDates(toDate = dateTo)
        }
    }
}


