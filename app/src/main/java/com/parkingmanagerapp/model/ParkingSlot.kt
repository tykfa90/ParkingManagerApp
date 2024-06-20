package com.parkingmanagerapp.model

data class ParkingSlot(
    var parkingSlotID: String = "",
    var parkingSlotLabel: String = "",
    var annotation: String = ""
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this(
        parkingSlotID = "",
        parkingSlotLabel = "",
        annotation = ""
    )
}