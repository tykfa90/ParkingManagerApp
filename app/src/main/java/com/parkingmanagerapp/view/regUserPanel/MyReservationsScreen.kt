package com.parkingmanagerapp.view.regUserPanel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.parkingmanagerapp.model.Reservation
import com.parkingmanagerapp.ui.theme.ListScreenLayout
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.view.regUserPanel.reservationSystem.CancelReservationConfirmationDialog
import com.parkingmanagerapp.view.regUserPanel.reservationSystem.ReservationItem
import com.parkingmanagerapp.viewModel.AuthViewModel
import com.parkingmanagerapp.viewModel.ReservationViewModel

@Composable
fun MyReservationsScreen(
    navController: NavController,
    reservationViewModel: ReservationViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val user by authViewModel.user.collectAsState()
    val reservations by reservationViewModel.userReservations.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedReservation by remember { mutableStateOf<Reservation?>(null) }

    LaunchedEffect(user) {
        if (user != null) {
            reservationViewModel.loadUserReservations(user!!.uid)
        }
    }

    StandardScreenLayout(
        title = "My Reservations",
        snackbarHostState = snackbarHostState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ListScreenLayout(
                    listItems = reservations,
                    isAdminContext = false,
                    onEdit = {}, // No-op for regular users
                    onDelete = {}, // No-op for regular users
                    itemContent = { reservation, _, _, _, modifier ->
                        ReservationItem(
                            reservation = reservation,
                            onCancelClick = {
                                selectedReservation = reservation
                                showDialog = true
                            },
                            modifier = modifier
                        )
                    }
                )
            }
        }
    }

    if (showDialog && selectedReservation != null) {
        CancelReservationConfirmationDialog(
            reservation = selectedReservation!!,
            onConfirm = {
                reservationViewModel.cancelReservation(selectedReservation!!.reservationID)
                showDialog = false
                reservationViewModel.loadUserReservations(user!!.uid) // Refresh reservations
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
}