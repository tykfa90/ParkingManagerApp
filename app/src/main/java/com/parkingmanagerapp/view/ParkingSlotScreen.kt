package com.parkingmanagerapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.repository.ParkingSlotRepository
import com.parkingmanagerapp.ui.theme.ParkingManagerAppTheme
import com.parkingmanagerapp.viewModel.ParkingSlotViewModel

@Composable
fun ParkingSlotScreen(viewModel: ParkingSlotViewModel) {

    Text(
        text = "Parking Slot Screen - Active!"
    )

    val parkingSlots by viewModel.parkingSlots.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(parkingSlots) { parkingSlot ->
            ParkingSlotItem(parkingSlot = parkingSlot)
        }
    }
}

@Composable
fun ParkingSlotItem(parkingSlot: ParkingSlot) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(if (parkingSlot.isOccupied) Color.LightGray else Color.Green)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = parkingSlot.slotLabel,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Occupied status: " + parkingSlot.isOccupied.toString(),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ParkingSlotScreenPreview() {
    ParkingManagerAppTheme {
        Text(text = "I'm parking slot screen preview!")
        ParkingSlotScreen(ParkingSlotViewModel(ParkingSlotRepository()))
    }
}