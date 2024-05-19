package com.parkingmanagerapp.view.adminPanel.parkingSlotManagement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.ui.theme.ListScreenLayout
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.viewModel.ParkingSlotViewModel

@Composable
fun AdminParkingSlotScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: ParkingSlotViewModel = hiltViewModel()
) {
    val parkingSlots by viewModel.parkingSlots.collectAsState()
    var selectedSlot by remember { mutableStateOf<ParkingSlot?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    StandardScreenLayout(
        title = "Manage Parking Slots",
        snackbarHostState = snackbarHostState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ListScreenLayout(
                listItems = parkingSlots,
                onEdit = {
                    selectedSlot = it
                    showEditDialog = true
                },
                onDelete = {
                    selectedSlot = it
                    showDeleteDialog = true
                }
            )

            // Add Parking Slot Button
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }

        // Add Parking Slot Dialog
        if (showAddDialog) {
            AddParkingSlotDialog(
                onDismiss = { showAddDialog = false },
                onSave = { newSlot ->
                    viewModel.addNewParkingSlot(newSlot)
                    showAddDialog = false
                }
            )
        }

        // Edit Parking Slot Dialog
        if (showEditDialog && selectedSlot != null) {
            EditParkingSlotDialog(
                parkingSlot = selectedSlot!!,
                onDismiss = { showEditDialog = false },
                onSave = { updatedSlot ->
                    viewModel.modifyParkingSlot(updatedSlot.slotID, updatedSlot)
                    showEditDialog = false
                }
            )
        }

        // Delete user confirmation dialog
        if (showDeleteDialog && selectedSlot != null) {
            DeleteParkingSlotConfirmationDialog(
                parkingSlot = selectedSlot!!,
                onDismiss = { showDeleteDialog = false },
                onConfirm = {
                    viewModel.removeParkingSlot(selectedSlot!!.slotID)
                    showDeleteDialog = false
                }
            )
        }
    }
}

@Composable
fun EditParkingSlotDialog(
    parkingSlot: ParkingSlot,
    onDismiss: () -> Unit,
    onSave: (ParkingSlot) -> Unit
) {
    var slotLabel by remember { mutableStateOf(parkingSlot.slotLabel) }
    var isOccupied by remember { mutableStateOf(parkingSlot.isOccupied) }

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
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(parkingSlot.copy(slotLabel = slotLabel, isOccupied = isOccupied))
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