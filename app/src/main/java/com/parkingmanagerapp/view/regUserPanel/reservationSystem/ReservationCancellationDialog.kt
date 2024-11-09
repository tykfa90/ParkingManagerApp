package com.parkingmanagerapp.view.regUserPanel.reservationSystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        title = {
            Text(text = "Cancel Reservation")
        },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "Parking Slot: $slotLabel")
                Text(text = "Start Date: $formattedStartDate")
                Text(text = "End Date: $formattedEndDate")
                Text(text = "Are you sure you want to cancel this reservation?")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onCancel()
                    // Dismissing the dialog after executing the cancel operation.
                    onDismiss()
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text("Dismiss")
            }
        }
    )
}