package com.parkingmanagerapp.view.adminPanel.reservationManagement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.parkingmanagerapp.model.Reservation
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.view.regUserPanel.reservationSystem.ReservationCancellationDialog
import com.parkingmanagerapp.view.regUserPanel.reservationSystem.ReservationItem
import com.parkingmanagerapp.viewModel.ReservationViewModel

@Composable
fun AdminReservationsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    reservationViewModel: ReservationViewModel = hiltViewModel(),
) {
    val reservations by reservationViewModel.allReservations.collectAsState()
    var selectedReservation by remember { mutableStateOf<Reservation?>(null) }
    var showCancelDialog by remember { mutableStateOf(false) }
    val parkingSlotLabels by reservationViewModel.parkingSlotLabels.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    // Fetch all reservations on screen load
    LaunchedEffect(Unit) {
        reservationViewModel.fetchReservations()
    }

    StandardScreenLayout(
        title = "Manage Reservations",
        snackbarHostState = snackbarHostState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                reservations.forEach { reservation ->
                    val slotLabel = parkingSlotLabels[reservation.parkingSlotID] ?: "Unknown"
                    ReservationItem(
                        reservation = reservation,
                        slotLabel = slotLabel,
                        onCancelClick = {
                            selectedReservation = reservation
                            showDialog = true
                        }
                    )
                }
            }


            // Cancel Reservation Confirmation Dialog
            if (showCancelDialog && selectedReservation != null) {
                ReservationCancellationDialog(
                    reservation = selectedReservation!!,
                    slotLabel = selectedReservation!!.parkingSlotID,
                    onCancel = {
                        reservationViewModel.cancelReservation(selectedReservation!!.reservationID)
                        showCancelDialog = false
                    },
                    onDismiss = {
                        showCancelDialog = false
                    },
                    onComplete = {
                        showDialog = false
                    }

                )
            }
        }
    }
}