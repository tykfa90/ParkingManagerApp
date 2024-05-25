package com.parkingmanagerapp.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.view.adminPanel.parkingSlotManagement.ParkingSlotItem

// Specialised layout for the lists within the application
@Composable
fun ListScreenLayout(
    listItems: List<ParkingSlot>,
    isAdminContext: Boolean,
    onEdit: (ParkingSlot) -> Unit,
    onDelete: (ParkingSlot) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(listItems) { parkingSlot ->
            ParkingSlotItem(
                parkingSlot = parkingSlot,
                isAdminContext = isAdminContext,
                onEdit = { onEdit(parkingSlot) },
                onDelete = { onDelete(parkingSlot) }
            )
        }
    }
}