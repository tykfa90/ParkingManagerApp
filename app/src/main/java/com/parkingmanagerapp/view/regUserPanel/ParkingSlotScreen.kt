package com.parkingmanagerapp.view.regUserPanel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.parkingmanagerapp.ui.theme.ListScreenLayout
import com.parkingmanagerapp.viewModel.ParkingSlotViewModel

@Composable
fun ParkingSlotScreen(
    navController: NavController,
    viewModel: ParkingSlotViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val parkingSlots by viewModel.parkingSlots.collectAsState()

    ListScreenLayout(
        listItems = parkingSlots,
        onEdit = {}, // No-op for regular users
        onDelete = {} // No-op for regular users
    )
}