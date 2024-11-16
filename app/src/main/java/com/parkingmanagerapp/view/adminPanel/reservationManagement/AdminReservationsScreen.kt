package com.parkingmanagerapp.view.adminPanel.reservationManagement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
            var isScrolledToEnd by remember { mutableStateOf(false) }

            // Sort reservations by start date
            val sortedReservations = reservations.sortedBy { it.reservationStart }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 48.dp)
            ) {
                items(sortedReservations) { reservation ->
                    val slotLabel = parkingSlotLabels[reservation.parkingSlotID] ?: "Unknown"
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, shape = MaterialTheme.shapes.medium),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        ReservationItem(
                            reservation = reservation,
                            slotLabel = slotLabel,
                            onCancelClick = {
                                selectedReservation = reservation
                                showCancelDialog = true
                            },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                // Set isScrolledToEnd to true if there are more items beyond visible space
                if (sortedReservations.size > 3) {
                    isScrolledToEnd = false
                } else {
                    isScrolledToEnd = true
                }
            }

            // Scroll indicator to inform admins they can scroll down/up
            if (!isScrolledToEnd && sortedReservations.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Scroll down for more",
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.Gray, CircleShape)
                            .padding(4.dp),
                        tint = Color.White
                    )
                }
            }

            // Cancel Reservation Confirmation Dialog
            if (showCancelDialog && selectedReservation != null) {
                ReservationCancellationDialog(
                    reservation = selectedReservation!!,
                    slotLabel = parkingSlotLabels[selectedReservation!!.parkingSlotID] ?: "Unknown",
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