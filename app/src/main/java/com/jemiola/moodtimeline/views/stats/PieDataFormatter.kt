package com.jemiola.moodtimeline.views.stats

import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.math.RoundingMode

class PieDataFormatter(private val allValuesSum: Int) : ValueFormatter() {

    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
        val pieLabelValue =
            (value / allValuesSum * 100).toBigDecimal().setScale(1, RoundingMode.DOWN).toString()
        val withoutZeroAfterDot = if (pieLabelValue.contains(".0"))
            pieLabelValue.substring(0, pieLabelValue.length - 2)
        else pieLabelValue
        return "$withoutZeroAfterDot%"
    }
}