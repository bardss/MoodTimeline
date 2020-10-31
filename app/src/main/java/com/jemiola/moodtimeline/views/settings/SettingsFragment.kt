package com.jemiola.moodtimeline.views.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentSettingsBinding
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.AppThemeHandler
import com.jemiola.moodtimeline.utils.ResUtil
import com.jemiola.moodtimeline.utils.pdfgenerator.PDF_GENERATOR_ENVIRONMENT_DIR
import com.jemiola.moodtimeline.utils.rangepickers.RangePickersUtil
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

private const val ANIM_DURATION = 500

class SettingsFragment : BaseFragment(), SettingsContract.View {

    override val presenter: SettingsPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentSettingsBinding
    private val rangePickersUtil = RangePickersUtil()
    private val appThemeHandler = AppThemeHandler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::binding.isInitialized) {
            binding = FragmentSettingsBinding.inflate(inflater, container, false)
            setupChangeThemeButton()
            setupGeneratePdfButton()
            presenter.setupCurrentThemeText()
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

    override fun toggleExportMoodsDialogVisibility() {
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

    override fun setCurrentThemeText(appThemeFromDatabase: Int?) {
        val appTheme = appThemeFromDatabase ?: appThemeHandler.getCurrentNightMode(resources)
        val themeText = when (appTheme) {
            AppCompatDelegate.MODE_NIGHT_NO -> ResUtil.getString(resources, R.string.light)
            AppCompatDelegate.MODE_NIGHT_YES -> ResUtil.getString(resources, R.string.dark)
            else -> ""
        }
        val themeIcon = when (appTheme) {
            AppCompatDelegate.MODE_NIGHT_NO -> ResUtil.getDrawable(context, R.drawable.ic_sun)
            AppCompatDelegate.MODE_NIGHT_YES -> ResUtil.getDrawable(context, R.drawable.ic_moon)
            else -> null
        }
        binding.appThemeValueTextView.text = themeText
        binding.appThemeValueImageView.setImageDrawable(themeIcon)
    }

    private fun setupChangeThemeButton() {
        binding.themeButtonView.setOnClickListener {
            val themeToSet =
                if (appThemeHandler.getCurrentNightMode(resources) == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.MODE_NIGHT_NO
                } else {
                    AppCompatDelegate.MODE_NIGHT_YES
                }
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
            AppCompatDelegate.setDefaultNightMode(themeToSet)
            presenter.saveAppTheme(themeToSet)
            restartApp()
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

    override fun showGeneratePdfSuccessDialog() {
        val storageDir = context?.getExternalFilesDir(PDF_GENERATOR_ENVIRONMENT_DIR)
        binding.infoDialogLayout.infoDialogTitleTextView.text =
            ResUtil.getString(resources, R.string.pdf_generated)
        val pdfGeneratedToText = ResUtil.getString(resources, R.string.pdf_generated_to)
        val content = "$pdfGeneratedToText\n\n$storageDir"
        binding.infoDialogLayout.infoDialogContentTextView.text = content
        AnimUtils.fadeIn(ANIM_DURATION, binding.infoDialogLayout.infoDialogContentLayout)
        binding.infoDialogLayout.closeTextView.setOnClickListener {
            AnimUtils.fadeOut(ANIM_DURATION, {
                toggleExportMoodsDialogVisibility()
                binding.infoDialogLayout.infoDialogContentLayout.visibility = View.GONE
            }, binding.infoDialogLayout.infoDialogContentLayout)
        }
    }
}
