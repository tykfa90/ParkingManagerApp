package com.parkingmanagerapp.model

data class ParkingSlot(
    var slotID: String,
    val slotLabel: String = "",
    val isOccupied: Boolean = false,
    val slotLength: Double,
    val slotWidth: Double,
    val slotHeight: Double
) {
    // No-argument constructor for Firestore deserialization purposes
    constructor() : this(
        slotID = "",
        slotLabel = "",
        isOccupied = false,
        slotLength = 0.0,
        slotWidth = 0.0,
        slotHeight = 0.0
    )
}