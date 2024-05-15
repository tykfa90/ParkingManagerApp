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
                document.toObject(ParkingSlot::class.java)?.apply {
                    slotID = document.id // Set the slotID from the document ID
                }
            }

        parkingSlots
    } catch (e: Exception) {
        Log.e("ParkingSlotError", "Error fetching parking slots", e)
        emptyList()
    }

    suspend fun updateParkingSlot(slotID: String, updatedSlot: ParkingSlot): Result<Unit> {
        return try {
            parkingSlotCollection.document(slotID)
                .set(updatedSlot).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteParkingSlot(slotID: String): Result<Unit> {
        return try {
            parkingSlotCollection.document(slotID)
                .delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}