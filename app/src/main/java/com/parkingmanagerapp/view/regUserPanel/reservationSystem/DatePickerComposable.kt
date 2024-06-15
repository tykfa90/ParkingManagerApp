package com.parkingmanagerapp.view.regUserPanel.reservationSystem

import android.app.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import java.util.Date

@Composable
fun DatePickerComposable(
    initialDate: Date,
    onDateSelected: (Date) -> Unit,
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    minDate: Date
) {
    if (showDialog) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        calendar.time = initialDate

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(calendar.time)
                onDismissRequest()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = minDate.time
        }.show()
    }
}