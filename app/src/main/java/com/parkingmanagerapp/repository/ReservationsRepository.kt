package com.parkingmanagerapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.parkingmanagerapp.model.Reservation
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class ReservationsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineContext
) {
    private val reservationsCollection = firestore.collection("reservations")

    // Fetches reservations by userID
    suspend fun getUserReservations(userID: String): Result<List<Reservation>> = withContext(ioDispatcher) {
        try {
            val snapshot = reservationsCollection
                .whereEqualTo("userID", userID)
                .get()
                .await()
            Result.success(snapshot.documents.mapNotNull { it.toObject(Reservation::class.java) })
        } catch (e: Exception) {
            Log.e("ReservationsRepository", "Error fetching user reservations: ${e.message}")
            Result.failure(e)
        }
    }

    // Fetches all existing reservations
    suspend fun getAllReservations(): Result<List<Reservation>> = withContext(ioDispatcher) {
        try {
            val snapshot = reservationsCollection.get().await()
            Result.success(snapshot.documents.mapNotNull { it.toObject(Reservation::class.java) })
        } catch (e: Exception) {
            Log.e("ReservationsRepository", "Error fetching all reservations: ${e.message}")
            Result.failure(e)
        }
    }

    // Adds a new reservation using Firestore transactions to maintain consistency
    suspend fun addReservation(reservation: Reservation): Result<Boolean> = withContext(ioDispatcher) {
        try {
            // Fetch existing reservations before starting the transaction
            val existingReservationsSnapshot = reservationsCollection
                .whereEqualTo("parkingSlotID", reservation.parkingSlotID)
                .get()
                .await()

            val existingReservations = existingReservationsSnapshot.toObjects(Reservation::class.java)

            // Run the transaction
            val result = firestore.runTransaction { transaction ->
                // Check for any overlapping reservations
                val hasOverlap = existingReservations.any { existingReservation ->
                    isOverlapping(
                        reservation.reservationStart,
                        reservation.reservationEnd,
                        existingReservation.reservationStart,
                        existingReservation.reservationEnd
                    )
                }

                if (!hasOverlap) {
                    // If no overlap, add the reservation
                    val newReservationRef = reservationsCollection.document()
                    transaction.set(newReservationRef, reservation)
                    true // Successfully added the reservation
                } else {
                    false // Overlapping reservation found
                }
            }.await() // Await the transaction itself

            Result.success(result)
        } catch (e: Exception) {
            Log.e("ReservationsRepository", "Error adding reservation: ${e.message}")
            Result.failure(e)
        }
    }

    // Helper function to check for overlapping reservations
    private fun isOverlapping(start1: Date, end1: Date, start2: Date, end2: Date): Boolean {
        return (start1 <= end2 && end1 >= start2)
    }

    // Removes the specified reservation from the system
    suspend fun deleteReservation(reservationID: String): Result<Boolean> = withContext(ioDispatcher) {
        try {
            Log.d("ReservationsRepository", "Attempting to delete reservation with ID: $reservationID")

            // Query to find the document with matching reservationID
            val querySnapshot = reservationsCollection
                .whereEqualTo("reservationID", reservationID)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                Log.d("ReservationsRepository", "No reservation found with ID: $reservationID")
                return@withContext Result.failure(Exception("Reservation not found"))
            }

            // Get the Firestore document ID and delete it
            val firestoreDocID = querySnapshot.documents[0].id
            reservationsCollection.document(firestoreDocID).delete().await()

            Log.d("ReservationsRepository", "Reservation deleted successfully for ID: $reservationID")
            Result.success(true)
        } catch (e: Exception) {
            Log.e("ReservationsRepository", "Error deleting reservation: ${e.message}")
            Result.failure(e)
        }
    }
}