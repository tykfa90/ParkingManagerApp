package com.parkingmanagerapp.model

import java.util.Date

data class Reservation(
    var reservationID: String = "",
    var parkingSlotID: String = "",
    var userID: String = "",
    var licensePlate: String = "",
    var reservationStart: Date = Date(),
    var reservationEnd: Date = Date()
)