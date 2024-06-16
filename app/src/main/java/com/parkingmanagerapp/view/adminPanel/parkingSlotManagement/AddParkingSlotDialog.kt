package com.parkingmanagerapp.view.adminPanel.parkingSlotManagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parkingmanagerapp.model.ParkingSlot

@Composable
fun AddParkingSlotDialog(
    onDismiss: () -> Unit,
    onSave: (ParkingSlot) -> Unit
) {
    var slotLabel by remember { mutableStateOf("") }
    var annotation by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add Parking Slot") },
        text = {
            Column {
                TextField(
                    value = slotLabel,
                    onValueChange = { slotLabel = it },
                    label = { Text("Slot Label") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = annotation,
                    onValueChange = { annotation = it },
                    label = { Text("Annotation") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val newSlot = ParkingSlot(
                    parkingSlotID = "", // This will be auto-generated by Firestore
                    parkingSlotLabel = slotLabel,
                    isOccupied = false, // Default to unoccupied
                    annotation = annotation
                )
                onSave(newSlot)
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