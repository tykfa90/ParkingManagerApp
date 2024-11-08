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

    // Adds parking slot to the database and assigns the document ID as the slotID
    suspend fun addParkingSlot(parkingSlot: ParkingSlot): Result<Unit> {
        return try {
            val documentRef = parkingSlotCollection.add(parkingSlot).await()
            val documentId = documentRef.id
            parkingSlotCollection.document(documentId).update("parkingSlotID", documentId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(
                "ParkingSlotRepository",
                "Error while adding new parking slot: ${e.localizedMessage}"
            )
            Result.failure(e)
        }
    }

    // Fetches all parking slots from database as a list
    suspend fun getAllParkingSlots(): Result<List<ParkingSlot>> {
        return try {
            val snapshot = db.collection("parkingSlots").get().await()
            Result.success(snapshot.toObjects(ParkingSlot::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Updates the selected parking spot
    suspend fun updateParkingSlot(slotID: String, updatedSlot: ParkingSlot): Result<Unit> {
        return try {
            parkingSlotCollection.document(slotID)
                .set(updatedSlot).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(
                "ParkingSlotRepository",
                "Error while updating parking slot: ${e.localizedMessage}"
            )
            Result.failure(e)
        }
    }

    suspend fun deleteParkingSlot(slotID: String): Result<Unit> {
        return try {
            parkingSlotCollection.document(slotID)
                .delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(
                "ParkingSlotRepository",
                "Error while deleting parking slot: ${e.localizedMessage}"
            )
            Result.failure(e)
        }
    }
}