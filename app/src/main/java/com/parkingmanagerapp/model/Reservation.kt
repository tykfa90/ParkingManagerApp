package com.parkingmanagerapp.model

import java.util.Date

data class Reservation(
    val reservationID: String,
    val parkingSpaceID: String,
    val userID: User,
    val licensePlate: String,
    val reservationStart: Date,
    val reservationEnd: Date
)