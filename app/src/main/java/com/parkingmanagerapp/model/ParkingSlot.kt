package com.parkingmanagerapp.model

data class ParkingSlot(
    var slotID: String = "", // Default value for slotID
    val slotLabel: String = "",
    val isOccupied: Boolean = false,
    val annotation: String = ""
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this(
        slotID = "",
        slotLabel = "",
        isOccupied = false,
        annotation = ""
    )
}