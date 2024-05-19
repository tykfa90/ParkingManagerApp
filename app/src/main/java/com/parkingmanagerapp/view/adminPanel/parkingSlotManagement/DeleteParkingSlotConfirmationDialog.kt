package com.parkingmanagerapp.view.adminPanel.parkingSlotManagement

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.parkingmanagerapp.model.ParkingSlot

@Composable
fun DeleteParkingSlotConfirmationDialog(
    parkingSlot: ParkingSlot,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete Parking Slot") },
        text = { Text(text = "Are you sure you want to delete ${parkingSlot.slotLabel}?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}