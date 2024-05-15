package com.parkingmanagerapp.ui.theme

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.viewModel.ParkingSlotViewModel

// Standard screen layout ordering the default hierarchy of displayed elements on screen.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardScreenLayout(
    title: String,
    snackbarHostState: SnackbarHostState,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    content(paddingValues)
                }
            }
        }
    )
}

// Specialised layout for the lists within the application
@Composable
fun ListScreenLayout(
    modifier: Modifier = Modifier,
    listItems: List<ParkingSlot>,
    onEdit: (ParkingSlot) -> Unit,
    onDelete: (ParkingSlot) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.fillMaxSize()
        ) {
            items(listItems) { parkingSlot ->
                ParkingSlotItem(
                    parkingSlot = parkingSlot,
                    onEdit = onEdit,
                    onDelete = onDelete
                )
            }
        }
    }
}

@Composable
fun ParkingSlotItem(
    parkingSlot: ParkingSlot,
    onEdit: (ParkingSlot) -> Unit,
    onDelete: (ParkingSlot) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit(parkingSlot) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = parkingSlot.slotLabel,
                style = MaterialTheme.typography.bodySmall,
            )
            Row {
                IconButton(onClick = { onEdit(parkingSlot) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = { onDelete(parkingSlot) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@Composable
fun AdminParkingSlotManagementScreen(
    viewModel: ParkingSlotViewModel,
    snackbarHostState: SnackbarHostState
) {
    val parkingSlots by viewModel.parkingSlots.collectAsState()
    var selectedSlot by remember { mutableStateOf<ParkingSlot?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    StandardScreenLayout(
        title = "Manage Parking Slots",
        snackbarHostState = snackbarHostState
    ) { paddingValues ->
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
                onClick = { /* Show dialog or navigate to add slot screen */ },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
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
            DeleteConfirmationDialog(
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
    // Implement dialog UI for editing parking slot
    // Include fields for slotLabel, isOccupied, etc.
}

@Composable
fun DeleteConfirmationDialog(
    parkingSlot: ParkingSlot,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete Parking Slot") },
        text = { Text(text = "Are you sure you want to delete this parking slot?") },
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