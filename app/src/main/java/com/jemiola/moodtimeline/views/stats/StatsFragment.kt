package com.jemiola.moodtimeline.views.stats

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentStatsBinding
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
            setupDummyPieChart()
            setupRangeMultipartButton()
        }
        return binding.root
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
        Log.e("Range", range.toString())
        val dateFrom = when (range) {
            YEAR -> dateTo.minusYears(1)
            MONTH -> dateTo.minusMonths(1)
            WEEK -> dateTo.minusDays(7)
        }
        val locale = LocaleUtil.getSystemLocale(requireContext())
        val dateToFormatted = dateFormatter.getFormattedDate(locale, dateTo)
        val dateFromFormatted = dateFormatter.getFormattedDate(locale, dateFrom)
        binding.fromEditText.setText(dateFromFormatted)
        binding.toEditText.setText(dateToFormatted)
    }

    private fun setupDummyPieChart() {
        val label = "Label"
        val entries = mutableListOf<PieEntry>()
        entries.add(PieEntry(120f))
        entries.add(PieEntry(100f))
        entries.add(PieEntry(50f))
        entries.add(PieEntry(250f))
        entries.add(PieEntry(70f))
        val dataSet = PieDataSet(entries, label)
        dataSet.colors = getPieChartColors(requireContext())
        val data = PieData(dataSet)
        data.setDrawValues(true)
        binding.chartView.data = data
        binding.chartView.setUsePercentValues(true)
        binding.chartView.isHighlightPerTapEnabled = true
        binding.chartView.isRotationEnabled = false
        binding.chartView.invalidate()
    }

    private fun getPieChartColors(context: Context) = mutableListOf(
        ResUtil.getColor(context, R.color.colorMoodVeryGood),
        ResUtil.getColor(context, R.color.colorMoodGood),
        ResUtil.getColor(context, R.color.colorMoodMediocre),
        ResUtil.getColor(context, R.color.colorMoodBad),
        ResUtil.getColor(context, R.color.colorMoodVeryBad)
    )
}
