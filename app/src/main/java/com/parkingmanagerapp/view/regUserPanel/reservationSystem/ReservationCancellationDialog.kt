package com.parkingmanagerapp.view.regUserPanel.reservationSystem

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.parkingmanagerapp.model.Reservation
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ReservationCancellationDialog(
    reservation: Reservation,
    slotLabel: String,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    val dateTimeFormat = SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault())
    val formattedStartDate = dateTimeFormat.format(reservation.reservationStart)
    val formattedEndDate = dateTimeFormat.format(reservation.reservationEnd)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cancel Reservation") },
        text = {
            Column {
                Text(text = "Parking Slot: $slotLabel")
                Text(text = "Start Date: $formattedStartDate")
                Text(text = "End Date: $formattedEndDate")
            }
        },
        confirmButton = {
            Button(onClick = onCancel) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    )
}