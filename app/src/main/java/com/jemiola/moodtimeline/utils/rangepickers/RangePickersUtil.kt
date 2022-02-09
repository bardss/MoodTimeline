package com.jemiola.moodtimeline.utils.rangepickers

import android.app.DatePickerDialog
import android.content.Context
import android.text.Editable
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.jemiola.moodtimeline.utils.DateFormatterUtil
import com.jemiola.moodtimeline.utils.DefaultTime
import com.jemiola.moodtimeline.utils.LocaleUtil
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import java.util.*

class RangePickersUtil {

    private val dateFormatter = DateFormatterUtil()

    fun setupRangeCalendars(
        context: Context,
        fromEditText: EditText,
        toEditText: EditText,
        onChangeValueAction: (() -> Unit)? = null,
        onEditTextClick: (() -> Unit)? = null
    ) {
        val pickerFrom = createDatePicker(context, fromEditText)
        fromEditText.setOnClickListener {
            onEditTextClick?.invoke()
            pickerFrom.show()
        }
        val pickerTo = createDatePicker(context, toEditText)
        toEditText.setOnClickListener {
            onEditTextClick?.invoke()
            pickerTo.show()
        }
        setupDatePickerBlockades(pickerFrom, pickerTo, fromEditText, toEditText)
        setupSearchTextWatchers(pickerFrom, pickerTo, fromEditText, toEditText, onChangeValueAction)
    }

    private fun createOnDatePickedListener(editText: EditText) =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val locale = LocaleUtil.getSystemLocale(editText.context)
            val dateText =
                dateFormatter.createDateTextFrom(locale, dayOfMonth, monthOfYear + 1, year)
            editText.setText(dateText)
        }

    private fun createDatePicker(context: Context, editText: EditText): DatePickerDialog {
        return DatePickerDialog(
            context,
            createOnDatePickedListener(editText),
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun setupSearchTextWatchers(
        fromDatePicker: DatePickerDialog,
        toDatePicker: DatePickerDialog,
        fromEditText: EditText,
        toEditText: EditText,
        onChangeValueAction: (() -> Unit)? = null
    ) {
        val afterTextChangedAction = { _: Editable? ->
            val fromText = fromEditText.text
            val toText = toEditText.text
            if (fromText?.isNotEmpty() == true && toText?.isNotEmpty() == true) {
                onChangeValueAction?.invoke()
            }
            setupDatePickerBlockades(fromDatePicker, toDatePicker, fromEditText, toEditText)
        }
        fromEditText.doAfterTextChanged(afterTextChangedAction)
        toEditText.doAfterTextChanged(afterTextChangedAction)
    }

    private fun setupDatePickerBlockades(
        fromDatePicker: DatePickerDialog,
        toDatePicker: DatePickerDialog,
        fromEditText: EditText,
        toEditText: EditText
    ) {
        if (toEditText.text?.isNotEmpty() == true) {
            val toDate = getDateFromView(toEditText)
            fromDatePicker.datePicker.maxDate = getMilisFromDate(toDate)
        }
        if (fromEditText.text?.isNotEmpty() == true) {
            val fromDate = getDateFromView(fromEditText)
            toDatePicker.datePicker.minDate = getMilisFromDate(fromDate)
        }
    }

    private fun getDateFromView(editText: EditText): LocalDate {
        val locale = LocaleUtil.getSystemLocale(editText.context)
        val fromDateText = editText.text.toString()
        val formatter = dateFormatter.getDateFormat(locale)
        return LocalDate.parse(fromDateText, formatter)
    }

    private fun getMilisFromDate(date: LocalDate): Long {
        return LocalDateTime
            .of(date, LocalTime.NOON)
            .atZone(DefaultTime.getZone())
            .toInstant()
            .toEpochMilli()
    }
}