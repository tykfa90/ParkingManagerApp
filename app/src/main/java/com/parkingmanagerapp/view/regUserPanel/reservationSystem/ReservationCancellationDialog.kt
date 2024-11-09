package com.parkingmanagerapp.view.regUserPanel.reservationSystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parkingmanagerapp.model.Reservation
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ReservationCancellationDialog(
    reservation: Reservation,
    slotLabel: String,
    onCancel: (onComplete: () -> Unit) -> Unit,
    onDismiss: () -> Unit
) {
    val dateTimeFormat = SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault())
    val formattedStartDate = dateTimeFormat.format(reservation.reservationStart)
    val formattedEndDate = dateTimeFormat.format(reservation.reservationEnd)

    var isLoading by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cancel Reservation") },
        text = {
            Column {
                Text(text = "Parking Slot: $slotLabel")
                Text(text = "Start Date: $formattedStartDate")
                Text(text = "End Date: $formattedEndDate")
                if (isLoading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isLoading = true
                    onCancel {
                        isLoading = false
                        onDismiss()
                    }
                },
                enabled = !isLoading
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, enabled = !isLoading) {
                Text("Dismiss")
            }
        }
    )
}