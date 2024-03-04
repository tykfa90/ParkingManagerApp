package com.parkingmanagerapp.repository

import android.util.Log
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

    suspend fun getAllParkingSlots(): List<ParkingSlot> = try {
        val parkingSlots = parkingSlotCollection
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(ParkingSlot::class.java)
            }

        // Optional: Log each parking slot info here if necessary.
        parkingSlots.forEach { parkingSlot ->
            Log.d(
                "ParkingSlotInfo",
                "Label: ${parkingSlot.slotLabel}, Occupied: ${parkingSlot.isOccupied}"
            )
        }

        parkingSlots
    } catch (e: Exception) {
        Log.e("ParkingSlotError", "Error fetching parking slots", e)
        emptyList()
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