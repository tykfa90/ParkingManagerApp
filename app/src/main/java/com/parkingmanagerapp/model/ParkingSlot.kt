package com.parkingmanagerapp.model

import com.google.firebase.firestore.PropertyName

data class ParkingSlot(
    var slotID: String = "",
    var slotLabel: String = "",
    @get:PropertyName("occupied") @set:PropertyName("occupied") var isOccupied: Boolean = false,
    var annotation: String = ""
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this(
        slotID = "",
        slotLabel = "",
        isOccupied = false,
        annotation = ""
    )
}