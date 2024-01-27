package com.parkingmanagerapp.model

import java.util.Date

data class Reservation(
    val parkingSpaceID: Number,
    val bookedBy: User,
    val licensePlate: String,
    val reservationStart: Date,
    val reservationEnd: Date
)