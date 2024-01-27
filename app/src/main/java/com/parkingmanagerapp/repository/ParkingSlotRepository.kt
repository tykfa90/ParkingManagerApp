package com.parkingmanagerapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.parkingmanagerapp.model.ParkingSpace
import com.parkingmanagerapp.model.Reservation

class ParkingSlotRepository {

    private val db = FirebaseFirestore.getInstance()
    private val parkingSlotCollection = db.collection("parkingSlots")

    fun addParkingSlot(parkingSlot: ParkingSpace, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        parkingSlotCollection.add(parkingSlot)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.message ?: "Error adding parking slot") }
    }

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