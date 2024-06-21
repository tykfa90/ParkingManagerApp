package com.parkingmanagerapp.view.regUserPanel.reservationSystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.parkingmanagerapp.model.Reservation
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CancelReservationConfirmationDialog(
    reservation: Reservation,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
    val startDate = dateFormat.format(reservation.reservationStart)
    val endDate = dateFormat.format(reservation.reservationEnd)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Cancel Reservation") },
        text = {
            Column {
                Text(text = "Parking Slot: ${reservation.parkingSlotID}")
                Text(text = "Start Date: $startDate")
                Text(text = "End Date: $endDate")
                Text(text = "License Plate: ${reservation.licensePlate}")
                Text(text = "Are you sure you want to cancel this reservation?")
            }
        },
        confirmButton = {
            Button(onClick = onConfirm, modifier = Modifier.fillMaxWidth()) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}