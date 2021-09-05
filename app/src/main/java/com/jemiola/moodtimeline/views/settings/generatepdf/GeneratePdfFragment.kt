package com.jemiola.moodtimeline.views.settings.generatepdf

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.databinding.FragmentGeneratePdfBinding
import com.jemiola.moodtimeline.utils.pdfgenerator.PdfGeneratorFileManager
import com.jemiola.moodtimeline.utils.AnimUtils
import com.jemiola.moodtimeline.utils.ResUtil
import com.jemiola.moodtimeline.utils.pdfgenerator.PDF_GENERATOR_ENVIRONMENT_DIR
import com.jemiola.moodtimeline.utils.rangepickers.RangePickersUtil
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import java.io.File

private const val FAST_ANIM_DURATION = 200
private const val ANIM_DURATION = 500

class GeneratePdfFragment : BaseFragment(), GeneratePdfContract.View {

    override val presenter: GeneratePdfPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentGeneratePdfBinding
    private val rangePickersUtil = RangePickersUtil()
    private lateinit var generatePdfWorkManager: GeneratePdfWorkManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentGeneratePdfBinding.inflate(inflater, container, false)
            generatePdfWorkManager = GeneratePdfWorkManager(binding.root.context)
            setupGeneratePdfButton()
        }
        return binding.root
    }

    override fun onStart() {
        hideBottomMenu()
        super.onStart()
    }

    override fun onStop() {
        showBottomMenu()
        super.onStop()
    }

    private fun setupGeneratePdfButton() {
        binding.exportPdfDialogLayout.closeTextView.setOnClickListener {
            hideExportPdfDialog()
        }
        binding.exportPdfDialogLayout.exportAllMoodsView.setOnClickListener {
            generatePdfWithAllMoods()
        }
        binding.exportPdfDialogLayout.exportMoodsPeriodView.setOnClickListener {
            generatePdf()
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

    override fun hideExportPdfDialog() {
        val dialog = binding.exportPdfDialogLayout.exportPdfContainerLayout as View
        AnimUtils.fadeOut(ANIM_DURATION, {
            popFragment()
            dialog.visibility = View.GONE
        }, dialog)
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

    override fun showGeneratePdfSuccessDialog(pdfFile: File) {
        binding.successDialogLayout.successAnimationView.playAnimation()
        val storageDir = context?.getExternalFilesDir(PDF_GENERATOR_ENVIRONMENT_DIR)
        binding.successDialogLayout.infoDialogTitleTextView.text =
            ResUtil.getString(resources, R.string.pdf_generated)
        val pdfGeneratedToText = ResUtil.getString(resources, R.string.pdf_generated_to)
        val content = "$pdfGeneratedToText\n\n$storageDir"
        binding.successDialogLayout.infoDialogContentTextView.text = content
        AnimUtils.fadeIn(ANIM_DURATION, binding.successDialogLayout.successDialogContentLayout)
        binding.successDialogLayout.sharePdfView.setOnClickListener {
            shareGeneratedPdf(pdfFile)
        }
        binding.successDialogLayout.closeTextView.setOnClickListener {
            hideExportPdfSuccessDialog()
        }
    }

    private fun shareGeneratedPdf(pdfFile: File) {
        activity?.let {
            val uriToPdf = PdfGeneratorFileManager().getUriToPdf(it, pdfFile)
            val shareIntent = Intent().apply {
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                action = Intent.ACTION_SEND
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uriToPdf)
            }
            it.startActivity(shareIntent)
        }
    }

    private fun hideExportPdfSuccessDialog() {
        AnimUtils.fadeOut(ANIM_DURATION, {
            hideExportPdfDialog()
            binding.successDialogLayout.successDialogContentLayout.visibility = View.GONE
            popFragment()
        }, binding.successDialogLayout.successDialogContentLayout)
    }

    override fun showGeneratingPdfLoading() {
        AnimUtils.fadeIn(
            FAST_ANIM_DURATION,
            binding.inProgressDialogLayout.inProgressDialogContentLayout
        )
        binding.inProgressDialogLayout.successAnimationView.playAnimation()
    }

    override fun stopGeneratingPdfLoading() {
        AnimUtils.fadeOut(
            FAST_ANIM_DURATION,
            binding.inProgressDialogLayout.inProgressDialogContentLayout
        )
        binding.inProgressDialogLayout.successAnimationView.cancelAnimation()
    }

    private fun generatePdfWithAllMoods() {
        presenter.setMinMaxRangeDates {
            generatePdf()
        }
    }

    private fun generatePdf() {
        showGeneratingPdfLoading()
        val fromText = binding.exportPdfDialogLayout.fromEditText.text.toString()
        val toText = binding.exportPdfDialogLayout.fromEditText.text.toString()
        generatePdfWorkManager.runGeneratePdfRequest(this, fromText, toText) {
            stopGeneratingPdfLoading()
            showGeneratePdfSuccessDialog(it)
        }
    }
}