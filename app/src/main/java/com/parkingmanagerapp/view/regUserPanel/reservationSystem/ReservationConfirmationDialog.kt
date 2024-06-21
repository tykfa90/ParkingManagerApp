package com.parkingmanagerapp.view.regUserPanel.reservationSystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.parkingmanagerapp.model.Reservation
import com.parkingmanagerapp.viewModel.ReservationViewModel
import java.text.SimpleDateFormat
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
    viewModel: ReservationViewModel
) {
    var licensePlate by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(true) }

    val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
    val startDateFormatted = dateFormat.format(startDate)
    val endDateFormatted = dateFormat.format(endDate)

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Reservation") },
            text = {
                Column {
                    Text(text = "Parking Slot: $parkingSlotLabel")
                    Text(text = "Start Date: $startDateFormatted")
                    Text(text = "End Date: $endDateFormatted")
                    OutlinedTextField(
                        value = licensePlate,
                        onValueChange = { licensePlate = it.uppercase().filter { char -> char.isLetterOrDigit() } },
                        label = { Text("License Plate") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val reservation = Reservation(
                            reservationID = UUID.randomUUID().toString(),
                            parkingSlotID = parkingSlotID,
                            userID = userID,
                            licensePlate = licensePlate,
                            reservationStart = startDate,
                            reservationEnd = endDate
                        )
                        viewModel.createReservation(reservation)
                        showDialog = false
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}