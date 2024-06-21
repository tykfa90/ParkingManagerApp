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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
    var searchPerformed by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val parkingSlots by parkingSlotViewModel.parkingSlots.collectAsState()
    val filteredSlots by remember {
        derivedStateOf {
            if (searchPerformed) {
                reservationViewModel.filterAvailableSlots(startDate, endDate, parkingSlots)
            } else {
                emptyList()
            }
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
                    Text(
                        "Select Start Date: ${
                            SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(startDate)
                        }"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { showEndDatePicker = true }) {
                    Text(
                        "Select End Date: ${
                            SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(endDate)
                        }"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
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

                        if (start.after(end)) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Start date cannot be after end date.")
                            }
                        } else {
                            searchPerformed = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Search Available Slots")
                }

                Spacer(modifier = Modifier.height(16.dp))

                ListScreenLayout(
                    listItems = filteredSlots,
                    isAdminContext = false,
                    onEdit = {}, // No-op for regular users
                    onDelete = {}, // No-op for regular users
                    itemContent = { parkingSlot, _, _, _, modifier ->
                        UnifiedParkingSlotItem(
                            parkingSlot = parkingSlot,
                            buttonText = "Reserve",
                            onButtonClick = {
                                selectedSlot = parkingSlot
                                showDialog = true
                            },
                            modifier = modifier
                        )
                    }
                )
            }
        }
    }

    if (showDialog && selectedSlot != null && authViewModel.user.collectAsState().value != null) {
        val user = authViewModel.user.collectAsState().value!!

        ReservationConfirmationDialog(
            navController = navController,
            parkingSlotID = selectedSlot!!.parkingSlotID,  // Pass the ID for storage
            parkingSlotLabel = selectedSlot!!.parkingSlotLabel,  // Pass the label for display
            userID = user.uid,
            startDate = startDate,
            endDate = endDate,
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