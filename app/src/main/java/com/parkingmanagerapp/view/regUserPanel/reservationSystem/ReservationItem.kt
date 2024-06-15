package com.parkingmanagerapp.view.regUserPanel.reservationSystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parkingmanagerapp.model.Reservation
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ReservationItem(
    reservation: Reservation,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = "Parking Slot: ${reservation.parkingSlotID}")
        Text(text = "Start Date: ${dateFormat.format(reservation.reservationStart)}")
        Text(text = "End Date: ${dateFormat.format(reservation.reservationEnd)}")
        Text(text = "License Plate: ${reservation.licensePlate}")

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onCancelClick, modifier = Modifier.fillMaxWidth()) {
            Text("Cancel Reservation")
        }
    }
}