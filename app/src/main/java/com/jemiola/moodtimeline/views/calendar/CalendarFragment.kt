package com.jemiola.moodtimeline.views.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentCalendarBinding
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
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

}