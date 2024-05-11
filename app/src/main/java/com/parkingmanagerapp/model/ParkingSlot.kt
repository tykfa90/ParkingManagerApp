package com.parkingmanagerapp.model

data class ParkingSlot(
    val slotID: String,
    val slotLabel: String = "",
    val isOccupied: Boolean = false,
    val slotLength: Double,
    val slotWidth: Double,
    val slotHeight: Double
)