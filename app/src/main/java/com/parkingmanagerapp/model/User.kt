package com.parkingmanagerapp.model

data class User(
    val name: String,
    val surname: String,
    val phoneNumber: Number,
    val email: String,
    val reservations: List<Reservation>
)