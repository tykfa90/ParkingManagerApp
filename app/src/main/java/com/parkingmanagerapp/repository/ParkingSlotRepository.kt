package com.parkingmanagerapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.parkingmanagerapp.model.ParkingSlot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ParkingSlotRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val coroutineContext: CoroutineContext
) {
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
                val slotID = document.getString("slotID") ?: ""
                val slotLabel = document.getString("slotLabel") ?: ""
                val isOccupied = document.getBoolean("isOccupied") ?: false
                val slotLength = document.getDouble("slotLength") ?: 0.0
                val slotWidth = document.getDouble("slotWidth") ?: 0.0
                val slotHeight = document.getDouble("slotHeight") ?: 0.0

                // Constructing the ParkingSlot object with extracted values
                ParkingSlot(
                    slotID = slotID,
                    slotLabel = slotLabel,
                    isOccupied = isOccupied,
                    slotLength = slotLength,
                    slotWidth = slotWidth,
                    slotHeight = slotHeight
                )
            }

        parkingSlots
    } catch (e: Exception) {
        Log.e("ParkingSlotError", "Error fetching parking slots", e)
        emptyList()
    }
}