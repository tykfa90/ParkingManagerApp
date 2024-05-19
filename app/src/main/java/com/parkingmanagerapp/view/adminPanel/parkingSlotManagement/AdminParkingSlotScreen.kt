package com.parkingmanagerapp.view.adminPanel.parkingSlotManagement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.view.adminPanel.EditParkingSlotDialog
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
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(parkingSlots) { parkingSlot ->
                    ParkingSlotItem(
                        parkingSlot = parkingSlot,
                        onEdit = {
                            selectedSlot = parkingSlot
                            showEditDialog = true
                        },
                        onDelete = {
                            selectedSlot = parkingSlot
                            showDeleteDialog = true
                        }
                    )
                }
            }

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

        // Delete Confirmation Dialog
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
fun ParkingSlotItem(
    parkingSlot: ParkingSlot,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = parkingSlot.slotLabel, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = if (parkingSlot.isOccupied) "Occupied" else "Available",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(text = parkingSlot.annotation, style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}