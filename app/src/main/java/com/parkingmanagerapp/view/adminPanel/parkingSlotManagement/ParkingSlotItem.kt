package com.parkingmanagerapp.view.adminPanel.parkingSlotManagement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parkingmanagerapp.model.ParkingSlot

@Composable
fun ParkingSlotItem(
    parkingSlot: ParkingSlot,
    isAdminContext: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = parkingSlot.parkingSlotLabel, style = MaterialTheme.typography.bodyLarge)
                Text(text = if (parkingSlot.isOccupied) "Occupied" else "Available", style = MaterialTheme.typography.bodyMedium)
                Text(text = parkingSlot.annotation, style = MaterialTheme.typography.bodyMedium)
            }
            if (isAdminContext) {
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}