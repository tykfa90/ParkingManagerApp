package com.parkingmanagerapp.model

import com.google.firebase.firestore.PropertyName

data class ParkingSlot(
    @PropertyName("slotLabel") val slotLabel: String = "",
    @PropertyName("isOccupied") val isOccupied: Boolean = false
)