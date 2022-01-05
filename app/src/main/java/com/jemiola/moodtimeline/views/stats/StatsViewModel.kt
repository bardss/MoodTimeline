package com.jemiola.moodtimeline.views.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jemiola.moodtimeline.model.data.local.MoodsCountByType
import com.jemiola.moodtimeline.model.data.local.TimelineMoodBOv2
import com.jemiola.moodtimeline.model.data.local.toMoodsCountByType
import com.jemiola.moodtimeline.repositorymvvm.MoodsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class StatsViewModel(moodsRepository: MoodsRepository) : ViewModel() {

    private val pieChartFromDate: MutableLiveData<LocalDate> = MutableLiveData(null)
    private val pieChartToDate: MutableLiveData<LocalDate> = MutableLiveData(null)
    private val _moodsCountByType: MutableLiveData<MoodsCountByType> = MutableLiveData(MoodsCountByType())
    val moodsCountByType: LiveData<MoodsCountByType>
        get() = _moodsCountByType

    init {
        pieChartFromDate.observeForever { fromDate ->
            val toDate = pieChartToDate.value
            if (toDate != null) {
                viewModelScope.launch(Dispatchers.Default) {
                    val moods = moodsRepository.getMoodsFor(fromDate, toDate)
                    _moodsCountByType.postValue(moods.toMoodsCountByType())
                }
            }
        }
        pieChartToDate.observeForever { toDate ->
            val fromDate = pieChartFromDate.value
            if (fromDate != null) {
                viewModelScope.launch(Dispatchers.Default) {
                    val moods = moodsRepository.getMoodsFor(fromDate, toDate)
                    _moodsCountByType.postValue(moods.toMoodsCountByType())
                }
            }
        }
    }

    fun setPieChartDateFrom(date: LocalDate) {
        pieChartFromDate.postValue(date)
    }

    fun setPieChartDateTo(date: LocalDate) {
        pieChartToDate.postValue(date)
    }
}