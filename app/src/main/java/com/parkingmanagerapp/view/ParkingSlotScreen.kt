package com.parkingmanagerapp.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.ui.theme.ListScreenLayout
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.viewModel.ParkingSlotViewModel

// Produces a list of available parking slots drawn from the database
@Composable
fun ParkingSlotScreen(viewModel: ParkingSlotViewModel = hiltViewModel()) {
    val parkingSlots by viewModel.parkingSlots.collectAsState()
    StandardScreenLayout(title = "Parking Slots") {

        // Using ListScreenLayout for consistent styling
        ListScreenLayout(
            listItems = parkingSlots,
            itemContent = { parkingSlot ->
                ParkingSlotItem(parkingSlot)
            }
        )
    }
}

// Function creating list items for the ParkingSlotScreen list
@Composable
fun ParkingSlotItem(parkingSlot: ParkingSlot) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor =
            if (parkingSlot.isOccupied) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = parkingSlot.slotLabel,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "Occupied status: ${parkingSlot.isOccupied}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}