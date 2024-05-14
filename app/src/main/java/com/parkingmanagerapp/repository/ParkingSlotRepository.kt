package com.parkingmanagerapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.parkingmanagerapp.model.ParkingSlot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ParkingSlotRepository @Inject constructor(
    db: FirebaseFirestore
) {
    private val parkingSlotCollection = db.collection("parkingSlots")

    // Adds new parking slot to the database.
    suspend fun addParkingSlot(parkingSlot: ParkingSlot): Result<Unit> {
        return try {
            parkingSlotCollection.add(parkingSlot.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ParkingSlotRep", "Error adding parking slot", e)
            Result.failure(e)
        }
    }

    // Fetches all parking slots from the database.
    suspend fun getAllParkingSlots(): List<ParkingSlot> = try {
        val parkingSlots = parkingSlotCollection
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(ParkingSlot::class.java)?.apply { slotID = document.id }
            }
        parkingSlots
    } catch (e: Exception) {
        Log.e("ParkingSlotRep", "Error fetching parking slots", e)
        emptyList()
    }

    // Updates the specific parking slot, selected by its unique database ID.
    suspend fun updateParkingSlot(slotID: String, updatedSlot: ParkingSlot): Result<Unit> {
        return try {
            parkingSlotCollection.document(slotID).set(updatedSlot.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ParkingSlotRep", "Error updating parking slot", e)
            Result.failure(e)
        }
    }

    // Deletes the specific parking slot, selected by its unique database ID.
    suspend fun deleteParkingSlot(slotID: String): Result<Unit> {
        return try {
            parkingSlotCollection.document(slotID).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ParkingSlotRep", "Error deleting parking slot", e)
            Result.failure(e)
        }
    }

    // Map function to convert ParkingSlot to Firestore format.
    private fun ParkingSlot.toMap(): Map<String, Any> {
        return mapOf(
            "slotLabel" to slotLabel,
            "isOccupied" to isOccupied,
            "slotLength" to slotLength,
            "slotWidth" to slotWidth,
            "slotHeight" to slotHeight
        )
    }
}