package com.parkingmanagerapp.view.regUserPanel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import java.util.Locale

@Composable
fun ReservationScreen(
    navController: NavController,
    reservationViewModel: ReservationViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    val parkingSlots by reservationViewModel.parkingSlots.collectAsState()
    val user by authViewModel.user.collectAsState()
    var selectedSlot by remember { mutableStateOf<ParkingSlot?>(null) }
    var showDialog by remember { mutableStateOf(false) }

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
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Start Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("End Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val start = dateFormat.parse(startDate)
                        val end = dateFormat.parse(endDate)
                        if (start != null && end != null) {
                            reservationViewModel.filterAvailableSlots(start, end)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Search Available Slots")
                }

                Spacer(modifier = Modifier.height(16.dp))

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

    if (showDialog && selectedSlot != null && user != null) {
        ReservationConfirmationDialog(
            navController = navController,
            parkingSlotID = selectedSlot!!.parkingSlotID,
            userID = user!!.uid,
            startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(startDate)!!,
            endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(endDate)!!,
            viewModel = reservationViewModel
        )
    }
}