package com.parkingmanagerapp.view.regUserPanel.reservationSystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.viewModel.AuthViewModel
import com.parkingmanagerapp.viewModel.ParkingSlotViewModel
import com.parkingmanagerapp.viewModel.ReservationViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ReservationScreen(
    navController: NavController,
    reservationViewModel: ReservationViewModel = hiltViewModel(),
    parkingSlotViewModel: ParkingSlotViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    var startDate by remember { mutableStateOf(Date()) }
    var endDate by remember { mutableStateOf(Date()) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedSlot by remember { mutableStateOf<ParkingSlot?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val parkingSlots by parkingSlotViewModel.parkingSlots.collectAsState()
    val filteredSlots by reservationViewModel.parkingSlots.collectAsState()
    val user by authViewModel.user.collectAsState()
    val reservationAdded by reservationViewModel.reservationAdded.collectAsState()
    var isScrolledToEnd by remember { mutableStateOf(false) }

    // Observe reservationAdded status to handle successful addition
    LaunchedEffect(reservationAdded) {
        if (reservationAdded == true) {
            navController.navigate("reservation_screen") {
                popUpTo("reservation_screen") { inclusive = true }
            }
            reservationViewModel.clearReservationAddedStatus() // Reset status after navigation
        } else if (reservationAdded == false) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Failed to add reservation. Please try again.")
            }
            reservationViewModel.clearReservationAddedStatus()
        }
    }

    LaunchedEffect(startDate, endDate, parkingSlots) {
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.time

        calendar.time = endDate
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val end = calendar.time

        if (!start.after(end)) {
            reservationViewModel.filterAvailableSlots(start, end, parkingSlots)
        }
    }

    StandardScreenLayout(
        title = "Reservations",
        snackbarHostState = snackbarHostState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { showStartDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Select Start Date: ${
                            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(startDate)
                        }"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showEndDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Select End Date: ${
                            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(endDate)
                        }"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxSize()) {
                    // Sort parking slots by label alphabetically
                    val sortedAvailableSlots = filteredSlots.sortedBy { it.parkingSlotLabel }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 48.dp)
                    ) {
                        items(sortedAvailableSlots) { parkingSlot ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(2.dp, shape = MaterialTheme.shapes.medium),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(text = "Parking Slot: ${parkingSlot.parkingSlotLabel}")

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Button(
                                        onClick = {
                                            selectedSlot = parkingSlot
                                            showDialog = true
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Reserve")
                                    }
                                }
                            }
                        }

                        // Set isScrolledToEnd to true if there are more items beyond visible space
                        isScrolledToEnd = sortedAvailableSlots.size <= 3
                    }

                    // Scroll indicator to inform admins they can scroll down/up
                    if (!isScrolledToEnd && sortedAvailableSlots.isNotEmpty()) {
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
                }

            }
        }
    }

    if (showDialog && selectedSlot != null && user != null) {
        val calendar = Calendar.getInstance()

        calendar.time = startDate
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDateAtMidnight = calendar.time

        calendar.time = endDate
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endDateAtMidnight = calendar.time

        ReservationConfirmationDialog(
            navController = navController,
            parkingSlotID = selectedSlot!!.parkingSlotID,  // Pass the ID for storage
            parkingSlotLabel = selectedSlot!!.parkingSlotLabel,  // Pass the label for display
            userID = user!!.uid,
            startDate = startDateAtMidnight,
            endDate = endDateAtMidnight,
            viewModel = reservationViewModel,
            snackbarHostState = snackbarHostState
        )
    }

    DatePickerComposable(
        initialDate = startDate,
        onDateSelected = { selectedStart ->
            startDate = selectedStart
            if (endDate.before(selectedStart)) {
                endDate = selectedStart
            }
        },
        showDialog = showStartDatePicker,
        onDismissRequest = { showStartDatePicker = false },
        minDate = Date()
    )

    DatePickerComposable(
        initialDate = endDate,
        onDateSelected = { selectedEnd ->
            endDate = if (selectedEnd.before(startDate)) {
                startDate
            } else {
                selectedEnd
            }
        },
        showDialog = showEndDatePicker,
        onDismissRequest = { showEndDatePicker = false },
        minDate = startDate
    )
}