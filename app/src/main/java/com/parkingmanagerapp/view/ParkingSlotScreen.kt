package com.parkingmanagerapp.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.ui.theme.ParkingManagerAppTheme
import com.parkingmanagerapp.viewModel.ParkingSlotViewModel

@Composable
fun ParkingSlotScreen(viewModel: ParkingSlotViewModel = viewModel()) {

    Text(
        text = "Parking Slot Screen - Active!"
    )

    val parkingSlots by viewModel.parkingSlots.collectAsState()

    Log.d("ParkingSlotViewModel", "Fetched slots: $parkingSlots")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        parkingSlots.forEach { slot ->
            ParkingSlotItem(slot)
        }
    }
}

@Composable
fun ParkingSlotItem(slot: ParkingSlot) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Slot Number: ${slot.slotLabel}")
            Text("Status: ${if (slot.isOccupied) "Occupied" else "Available"}")
        }
    }
}

@Composable
fun YourContent(text: String) {
    Text(text = text)
}

@Preview(showBackground = true)
@Composable
fun ParkingSlotScreenPreview() {
    ParkingManagerAppTheme {
        Text(text = "I'm parking slot screen!")
        ParkingSlotScreen()
    }
}