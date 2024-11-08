package com.parkingmanagerapp.view.adminPanel.parkingSlotManagement

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.parkingmanagerapp.model.ParkingSlot

@Composable
fun EditParkingSlotDialog(
    parkingSlot: ParkingSlot,
    onDismiss: () -> Unit,
    onSave: (ParkingSlot) -> Unit
) {
    var slotLabel by remember { mutableStateOf(parkingSlot.parkingSlotLabel) }
    var annotation by remember { mutableStateOf(parkingSlot.annotation) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Parking Slot") },
        text = {
            Column {
                TextField(
                    value = slotLabel,
                    onValueChange = { slotLabel = it },
                    label = { Text("Slot Label") }
                )
                TextField(
                    value = annotation,
                    onValueChange = { annotation = it },
                    label = { Text("Annotation") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    parkingSlot.copy(
                        parkingSlotLabel = slotLabel,
                        annotation = annotation
                    )
                )
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}