package com.parkingmanagerapp.repository

import com.parkingmanagerapp.model.ParkingSpace
import com.parkingmanagerapp.model.Reservation

class Repository {

    fun getAllParkingSpaces(): List<ParkingSpace>? {
        TODO("Implement DB logic.")
    }

    fun getAllReservations(): List<Reservation>? {
        TODO()
    }

    fun getUserReservations(): List<Reservation>? {
        TODO()
    }

    fun cancelReservation(reservation: Reservation) {
        TODO()
    }
}