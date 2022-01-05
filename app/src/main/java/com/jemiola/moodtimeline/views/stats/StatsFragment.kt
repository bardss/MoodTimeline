package com.jemiola.moodtimeline.views.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.jemiola.moodtimeline.views.stats.StatsRange.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate


class StatsFragment : BaseFragment() {

    private lateinit var binding: FragmentStatsBinding
    private val viewModel by viewModel<StatsViewModel>()
    private val dateFormatter = DateFormatterUtil()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentStatsBinding.inflate(inflater, container, false)
            setupPieChart()
            setupRangeMultipartButton()
        }
        setupMoodsObserver()
        binding.rangeMultipartButton.clickChildAt(0)
        return binding.root
    }

    private fun setupMoodsObserver() {
        viewModel.moodsCountByType.observe(viewLifecycleOwner) { moodsCountByType ->
            setPieChartData(moodsCountByType)
        }
    }

    private fun setupRangeMultipartButton() {
        binding.rangeMultipartButton.setButtonClickListener {
            when (it) {
                ResUtil.getString(resources, R.string.year) -> setChartRange(YEAR)
                ResUtil.getString(resources, R.string.month) -> setChartRange(MONTH)
                ResUtil.getString(resources, R.string.week) -> setChartRange(WEEK)
            }
        }
    }

    private fun setChartRange(range: StatsRange) {
        val dateTo = LocalDate.now()
        val dateFrom = when (range) {
            YEAR -> dateTo.minusYears(1)
            MONTH -> dateTo.minusMonths(1)
            WEEK -> dateTo.minusDays(7)
        }
        val locale = LocaleUtil.getSystemLocale(requireContext())
        val dateToFormatted = dateFormatter.getFormattedDate(locale, dateTo)
        val dateFromFormatted = dateFormatter.getFormattedDate(locale, dateFrom)
        val previousFromText = binding.fromEditText.text.toString()
        if (previousFromText != dateFromFormatted) {
            binding.fromEditText.setText(dateFromFormatted)
            viewModel.setPieChartDateFrom(dateFrom)
        }
        val previousToText = binding.fromEditText.text.toString()
        if (previousToText != dateToFormatted) {
            binding.toEditText.setText(dateToFormatted)
            viewModel.setPieChartDateTo(dateTo)
        }
    }

    private fun setupPieChart() {
        with (binding.chartView) {
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
}


