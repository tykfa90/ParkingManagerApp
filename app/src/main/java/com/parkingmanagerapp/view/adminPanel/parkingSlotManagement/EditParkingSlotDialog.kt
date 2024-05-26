package com.parkingmanagerapp.view.adminPanel.parkingSlotManagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.parkingmanagerapp.model.ParkingSlot

@Composable
fun EditParkingSlotDialog(
    parkingSlot: ParkingSlot,
    onDismiss: () -> Unit,
    onSave: (ParkingSlot) -> Unit
) {
    var slotLabel by remember { mutableStateOf(parkingSlot.slotLabel) }
    var isOccupied by remember { mutableStateOf(parkingSlot.isOccupied) }
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isOccupied,
                        onCheckedChange = { isOccupied = it }
                    )
                    Text(text = "Is Occupied")
                }
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
                        slotLabel = slotLabel,
                        isOccupied = isOccupied,
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