package com.parkingmanagerapp.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.parkingmanagerapp.ui.theme.ListScreenLayout
import com.parkingmanagerapp.viewModel.ParkingSlotViewModel

// Produces a list of available parking slots drawn from the database
@Composable
fun ParkingSlotScreen(viewModel: ParkingSlotViewModel = hiltViewModel()) {
    val parkingSlots by viewModel.parkingSlots.collectAsState()

    ListScreenLayout(
        listItems = parkingSlots,
        onEdit = {}, // No-op for regular users
        onDelete = {} // No-op for regular users
    )
}