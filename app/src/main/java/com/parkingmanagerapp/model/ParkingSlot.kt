package com.parkingmanagerapp.model

import com.google.firebase.firestore.PropertyName

data class ParkingSlot(
    var parkingSlotID: String = "",
    var parkingSlotLabel: String = "",
    @get:PropertyName("occupied") @set:PropertyName("occupied") var isOccupied: Boolean = false,
    var annotation: String = ""
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this(
        parkingSlotID = "",
        parkingSlotLabel = "",
        isOccupied = false,
        annotation = ""
    )
}