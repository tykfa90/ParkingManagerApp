package com.parkingmanagerapp.view.regUserPanel

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
import com.parkingmanagerapp.viewModel.AuthViewModel
import com.parkingmanagerapp.viewModel.ReservationViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MyReservationsScreen(
    navController: NavController,
    reservationViewModel: ReservationViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val user by authViewModel.user.collectAsState()
    val userReservations by reservationViewModel.userReservations.collectAsState()
    val parkingSlotLabels by reservationViewModel.parkingSlotLabels.collectAsState()
    var selectedReservation by remember { mutableStateOf<Reservation?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

    // Fetch user reservations when the user changes
    LaunchedEffect(user) {
        user?.uid?.let { reservationViewModel.fetchUserReservations(it) }
    }

    // Fetch user reservations on user change
    LaunchedEffect(user) {
        user?.uid?.let { reservationViewModel.fetchUserReservations(it) }
    }

    StandardScreenLayout(
        title = "My Reservations",
        snackbarHostState = snackbarHostState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                userReservations.forEach { reservation ->
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

            if (showDialog && selectedReservation != null) {
                val reservation = selectedReservation!!
                ReservationCancellationDialog(
                    reservation = reservation,
                    slotLabel = parkingSlotLabels[reservation.parkingSlotID] ?: "Unknown",
                    onCancel = {
                        reservationViewModel.cancelReservation(reservation.reservationID)
                        showDialog = false
                        selectedReservation = null // Clear selected reservation to avoid stale state
                    },
                    onDismiss = {
                        showDialog = false
                        selectedReservation = null // Clear selected reservation to avoid stale state
                    }
                )
            }
        }
    }
}