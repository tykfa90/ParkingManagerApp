package com.parkingmanagerapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.model.Reservation
import kotlinx.coroutines.tasks.await

class ParkingSlotRepository {

    private val db = FirebaseFirestore.getInstance()
    private val parkingSlotCollection = db.collection("parkingSlots")

    fun addParkingSlot(
        parkingSlot: ParkingSlot,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        parkingSlotCollection.add(parkingSlot)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.message ?: "Error adding parking slot") }
    }

    suspend fun getAllParkingSlots(): List<ParkingSlot> {
        return try {
            db.collection("parkingSlots")
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    document.toObject(ParkingSlot::class.java)
                }
        } catch (e: Exception) {
            emptyList()
        }
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