package com.jemiola.moodtimeline.views.stats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jemiola.moodtimeline.model.data.local.MoodsCountByType
import com.jemiola.moodtimeline.model.data.local.PieChartRange
import com.jemiola.moodtimeline.model.data.local.toMoodsCountByType
import com.jemiola.moodtimeline.repositorymvvm.MoodsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class StatsViewModel(private val moodsRepository: MoodsRepository) : ViewModel() {

    private val _pieChartRange: MutableLiveData<PieChartRange> = MutableLiveData()
    val pieChartRange: LiveData<PieChartRange>
        get() = _pieChartRange

    private val _moodsCountByType: MutableLiveData<MoodsCountByType> = MutableLiveData()
    val moodsCountByType: LiveData<MoodsCountByType>
        get() = _moodsCountByType

    fun onPieChartRangeChange() {
        viewModelScope.launch(Dispatchers.Default) {
            _pieChartRange.value?.let { pieChartRange ->
                val moods = moodsRepository.getMoodsFor(pieChartRange.from, pieChartRange.to)
                _moodsCountByType.postValue(moods.toMoodsCountByType())
            }
        }
    }

    fun setChartRange(range: StatsRange, dateNow: LocalDate) {
        var dateFrom = dateNow
        when (range) {
            StatsRange.ALL -> getMoodsFirstAndLastDate()
            StatsRange.YEAR -> dateFrom = dateNow.minusYears(1)
            StatsRange.MONTH -> dateFrom = dateNow.minusMonths(1)
            StatsRange.WEEK -> dateFrom = dateNow.minusDays(7)
        }
        if (range != StatsRange.ALL) {
            setPieChartRangeDates(dateFrom, dateNow)
        }
    }

    fun setPieChartRangeDates(
        fromDate: LocalDate? = null,
        toDate: LocalDate? = null
    ) {
        val previousRange = _pieChartRange.value
        val fromDateToRequest = fromDate ?: previousRange?.from
        val toDateToRequest = toDate ?: previousRange?.to
        if (fromDateToRequest != null && toDateToRequest != null) {
            Log.e("setPieChartRangeDates", "fromDateToRequest: $fromDateToRequest")
            Log.e("setPieChartRangeDates", "toDateToRequest: $toDateToRequest")
            _pieChartRange.postValue(PieChartRange(fromDateToRequest, toDateToRequest))
        }
    }

    private fun getMoodsFirstAndLastDate() {
        viewModelScope.launch(Dispatchers.Default) {
            val firstMoodDate = moodsRepository.getFirstMoodDate()
            val lastMoodDate = moodsRepository.getLastMoodDate()
            Log.e("getMoodsFirstAndLastDat", "firstMoodDate: $firstMoodDate")
            Log.e("getMoodsFirstAndLastDat", "lastMoodDate: $lastMoodDate")
            setPieChartRangeDates(firstMoodDate, lastMoodDate)
        }
    }
}