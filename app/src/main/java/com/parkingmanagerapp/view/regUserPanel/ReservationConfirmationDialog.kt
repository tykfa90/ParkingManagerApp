package com.parkingmanagerapp.view.regUserPanel

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.parkingmanagerapp.model.Reservation
import com.parkingmanagerapp.viewModel.ReservationViewModel
import java.util.Date
import java.util.UUID

@Composable
fun ReservationConfirmationDialog(
    navController: NavController,
    parkingSlotID: String,
    userID: String,
    startDate: Date,
    endDate: Date,
    viewModel: ReservationViewModel = hiltViewModel()
) {
    var licensePlate by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Reservation") },
            text = {
                Column {
                    Text(text = "Parking Slot ID: $parkingSlotID")
                    Text(text = "Start Date: $startDate")
                    Text(text = "End Date: $endDate")
                    OutlinedTextField(
                        value = licensePlate,
                        onValueChange = { licensePlate = it },
                        label = { Text("License Plate") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val reservationID = UUID.randomUUID().toString()
                        val reservation = Reservation(
                            reservationID = reservationID,
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