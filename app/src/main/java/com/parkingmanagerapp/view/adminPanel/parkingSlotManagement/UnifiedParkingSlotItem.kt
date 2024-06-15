package com.parkingmanagerapp.view.adminPanel.parkingSlotManagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parkingmanagerapp.model.ParkingSlot

@Composable
fun UnifiedParkingSlotItem(
    parkingSlot: ParkingSlot,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Slot Label: ${parkingSlot.parkingSlotLabel}")
            Text(text = "Occupied: ${if (parkingSlot.isOccupied) "Yes" else "No"}")
            Text(text = "Annotation: ${parkingSlot.annotation}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onButtonClick) {
                Text(text = buttonText)
            }
        }
    }
}