package com.parkingmanagerapp.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.lifecycle.viewmodel.compose.viewModel
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

    //Test print-out to check db connection temporarily.
    Log.d("ParkingSlotViewModel", "Fetched slots: $parkingSlots")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp)
    ) {
        items(parkingSlots) { ParkingSlot ->
            ParkingSlotItem(parkingSlot = ParkingSlot) }
    }
}

@Composable
fun ParkingSlotItem(parkingSlot: ParkingSlot) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(if (parkingSlot.isOccupied) Color.LightGray else Color.Green)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = parkingSlot.slotLabel,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = if (parkingSlot.isOccupied) Icons.Default.Lock else Icons.Default.CheckCircle,
            contentDescription = if (parkingSlot.isOccupied) "Occupied" else "Available",
            tint = if (parkingSlot.isOccupied) Color.Red else Color.Green
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ParkingSlotScreenPreview() {
    ParkingManagerAppTheme {
        Text(text = "I'm parking slot screen!")
        ParkingSlotScreen(ParkingSlotViewModel(ParkingSlotRepository()))
    }
}