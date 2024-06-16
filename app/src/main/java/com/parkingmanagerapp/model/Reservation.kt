package com.parkingmanagerapp.model

import java.util.Date

data class Reservation(
    val reservationID: String,
    val parkingSlotID: String,
    val userID: String,
    val licensePlate: String,
    val reservationStart: Date,
    val reservationEnd: Date
)