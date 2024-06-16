package com.parkingmanagerapp.view.adminPanel.parkingSlotManagement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
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
    viewModel.setViewingContextAsAdmin(true)
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
                isAdminContext = true,
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

        // Add New Parking Slot Dialog
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
                    viewModel.modifyParkingSlot(updatedSlot.parkingSlotID, updatedSlot)
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
                    viewModel.removeParkingSlot(selectedSlot!!.parkingSlotID)
                    showDeleteDialog = false
                }
            )
        }
    }
}