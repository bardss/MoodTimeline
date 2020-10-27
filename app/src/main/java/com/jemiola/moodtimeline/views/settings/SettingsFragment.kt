package com.jemiola.moodtimeline.views.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentSettingsBinding
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.rangepickers.RangePickersUtil
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

private const val ANIM_DURATION = 500

class SettingsFragment : BaseFragment(), SettingsContract.View {

    override val presenter: SettingsPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentSettingsBinding
    private val rangePickersUtil = RangePickersUtil()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::binding.isInitialized) {
            binding = FragmentSettingsBinding.inflate(inflater, container, false)
            setupChangeThemeButton()
            setupGeneratePdfButton()
        }
        return binding.root
    }

    private fun setupGeneratePdfButton() {
        binding.exportMoodsPdfButtonView.setOnClickListener {
            toggleExportMoodsDialogVisibility()
        }
        binding.exportPdfDialogLayout.closeTextView.setOnClickListener {
            toggleExportMoodsDialogVisibility()
        }
        binding.exportPdfDialogLayout.exportAllMoodsView.setOnClickListener {
            context?.let {
                presenter.generatePdfWithAllMoods(it)
            }
        }
        binding.exportPdfDialogLayout.exportMoodsPeriodView.setOnClickListener {
            context?.let {
                presenter.generatePdfWithRangeMoods(it)
            }
        }
        presenter.setMinMaxRangeDates()
    }

    override fun setupRangeEditTexts() {
        context?.let {
            val fromEditText = binding.exportPdfDialogLayout.fromEditText
            val toEditText = binding.exportPdfDialogLayout.toEditText
            rangePickersUtil.setupRangeCalendars(it, fromEditText, toEditText)
        }
    }

    private fun toggleExportMoodsDialogVisibility() {
        val dialog = binding.exportPdfDialogLayout.exportPdfContainerLayout as View
        if (dialog.visibility == View.GONE) {
            hideBottomMenu()
            AnimUtils.fadeIn(ANIM_DURATION, dialog)
        } else {
            showBottomMenu()
            AnimUtils.fadeOut(ANIM_DURATION, {
                showBottomMenu()
                dialog.visibility = View.GONE
            }, dialog)
        }
    }

    private fun setupChangeThemeButton() {
        binding.themeButtonView.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    override fun setFromRangeText(date: String) {
        binding.exportPdfDialogLayout.fromEditText.setText(date)
    }

    override fun setToRangeText(date: String) {
        binding.exportPdfDialogLayout.toEditText.setText(date)
    }

    override fun getFromRangeText(): String {
        return binding.exportPdfDialogLayout.fromEditText.text.toString()
    }

    override fun getToRangeText(): String {
        return binding.exportPdfDialogLayout.toEditText.text.toString()
    }
}
