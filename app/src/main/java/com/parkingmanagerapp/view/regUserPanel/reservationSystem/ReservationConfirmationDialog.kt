package com.parkingmanagerapp.view.regUserPanel.reservationSystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.parkingmanagerapp.model.Reservation
import com.parkingmanagerapp.viewModel.ReservationViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun ReservationConfirmationDialog(
    navController: NavController,
    parkingSlotID: String,
    parkingSlotLabel: String,
    userID: String,
    startDate: Date,
    endDate: Date,
    viewModel: ReservationViewModel,
    snackbarHostState: SnackbarHostState,
) {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    var licensePlate by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Reservation") },
            text = {
                Column {
                    Text(text = "Parking Slot: $parkingSlotLabel")
                    Text(text = "Start Date: ${dateFormat.format(startDate)}")
                    Text(text = "End Date: ${dateFormat.format(endDate)}")
                    OutlinedTextField(
                        value = licensePlate,
                        onValueChange = {
                            licensePlate =
                                it.uppercase(Locale.getDefault()).replace("[^A-Z0-9]".toRegex(), "")
                        },
                        label = { Text("License Plate") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (licensePlate.isNotBlank()) {
                            val reservation = Reservation(
                                reservationID = UUID.randomUUID().toString(),
                                parkingSlotID = parkingSlotID,
                                userID = userID,
                                licensePlate = licensePlate,
                                reservationStart = startDate,
                                reservationEnd = endDate
                            )
                            viewModel.createReservation(reservation) {
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

                                viewModel.filterAvailableSlots(
                                    start,
                                    end,
                                    viewModel.parkingSlots.value
                                )

                                showDialog = false
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please enter a license plate before confirming.")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}