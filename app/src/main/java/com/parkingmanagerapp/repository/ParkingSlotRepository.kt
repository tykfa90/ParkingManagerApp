package com.parkingmanagerapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.model.Reservation
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ParkingSlotRepository @Inject constructor(private val db: FirebaseFirestore) {

    private val parkingSlotCollection = db.collection("parkingSlots")

    suspend fun addParkingSlot(parkingSlot: ParkingSlot): Result<Unit> {
        return try {
            parkingSlotCollection.add(parkingSlot).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllParkingSlots(): List<ParkingSlot> = try {
        val parkingSlots = parkingSlotCollection
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                // Manually extracting each field to avoid automatic Firebase object mapping mistakes
                val slotLabel = document.getString("slotLabel") ?: ""
                val isOccupied = document.getBoolean("isOccupied") ?: false

                // Constructing the ParkingSlot object with extracted values
                ParkingSlot(
                    slotLabel = slotLabel,
                    isOccupied = isOccupied
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