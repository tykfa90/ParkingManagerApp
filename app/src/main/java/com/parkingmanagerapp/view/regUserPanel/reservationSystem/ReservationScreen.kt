package com.parkingmanagerapp.view.regUserPanel.reservationSystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.ui.theme.ListScreenLayout
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.view.adminPanel.parkingSlotManagement.UnifiedParkingSlotItem
import com.parkingmanagerapp.viewModel.AuthViewModel
import com.parkingmanagerapp.viewModel.ReservationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ReservationScreen(
    navController: NavController,
    reservationViewModel: ReservationViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    var startDate by remember { mutableStateOf(Date()) }
    var endDate by remember { mutableStateOf(Date()) }
    var parkingSlots by remember { mutableStateOf<List<ParkingSlot>>(emptyList()) }
    val user by authViewModel.user.collectAsState()
    var selectedSlot by remember { mutableStateOf<ParkingSlot?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var searchPerformed by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    LaunchedEffect(snackbarMessage) {
        if (snackbarMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(snackbarMessage)
            snackbarMessage = ""
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
                TextButton(onClick = { showStartDatePicker = true }) {
                    Text("Select Start Date: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(startDate)}")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { showEndDatePicker = true }) {
                    Text("Select End Date: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(endDate)}")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
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

                        if (start.after(end)) {
                            snackbarMessage = "Start date cannot be after end date."
                        } else {
                            searchPerformed = true
                            reservationViewModel.filterAvailableSlots(start, end)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Search Available Slots")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (searchPerformed) {
                    val filteredSlots by reservationViewModel.parkingSlots.collectAsState()
                    parkingSlots = filteredSlots

                    ListScreenLayout(
                        listItems = parkingSlots,
                        isAdminContext = false,
                        onEdit = {}, // No-op for regular users
                        onDelete = {}, // No-op for regular users
                        itemContent = { item, _, _, _, modifier ->
                            UnifiedParkingSlotItem(
                                parkingSlot = item,
                                buttonText = "Reserve",
                                onButtonClick = {
                                    selectedSlot = item
                                    showDialog = true
                                },
                                modifier = modifier
                            )
                        }
                    )
                }
            }
        }
    }

    if (showDialog && selectedSlot != null && user != null) {

        calendar.time = startDate
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDateAtMidnight: Date = calendar.time

        calendar.time = endDate
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endDateAtMidnight: Date = calendar.time

        ReservationConfirmationDialog(
            navController = navController,
            parkingSlotID = selectedSlot!!.parkingSlotID,
            userID = user!!.uid,
            startDate = startDateAtMidnight,
            endDate = endDateAtMidnight,
            viewModel = reservationViewModel
        )
    }

    DatePickerComposable(
        initialDate = startDate,
        onDateSelected = { startDate = it },
        showDialog = showStartDatePicker,
        onDismissRequest = { showStartDatePicker = false }
    )

    DatePickerComposable(
        initialDate = endDate,
        onDateSelected = { endDate = it },
        showDialog = showEndDatePicker,
        onDismissRequest = { showEndDatePicker = false }
    )
}