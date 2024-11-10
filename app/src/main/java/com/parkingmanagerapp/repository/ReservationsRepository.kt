package com.parkingmanagerapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.parkingmanagerapp.model.Reservation
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationsRepository @Inject constructor(
    firestore: FirebaseFirestore
) {
    private val reservationsCollection = firestore.collection("reservations")

    // Fetches reservations by userID
    suspend fun getUserReservations(userID: String): Result<List<Reservation>> = try {
        val snapshot = reservationsCollection
            .whereEqualTo("userID", userID)
            .get()
            .await()
        Result.success(snapshot.documents.mapNotNull { it.toObject(Reservation::class.java) })
    } catch (e: Exception) {
        Log.e("ReservationsRepository", "Error fetching user reservations: ${e.message}")
        Result.failure(e)
    }

    // Fetches all existing reservations
    suspend fun getAllReservations(): Result<List<Reservation>> = try {
        val snapshot = reservationsCollection.get().await()
        Result.success(snapshot.documents.mapNotNull { it.toObject(Reservation::class.java) })
    } catch (e: Exception) {
        Log.e("ReservationsRepository", "Error fetching all reservations: ${e.message}")
        Result.failure(e)
    }

    // Adds a new reservation using optimistic concurrency control
    suspend fun addReservation(reservation: Reservation): Result<Boolean> = try {
        // Perform the overlap check before attempting to add
        val hasOverlap = checkForOverlap(reservation)

        if (hasOverlap) {
            Result.success(false) // Slot already reserved
        } else {
            // Optimistic concurrency check to see if reservation is still available
            val snapshot = reservationsCollection
                .whereEqualTo("parkingSlotID", reservation.parkingSlotID)
                .whereEqualTo("reservationStart", reservation.reservationStart)
                .get()
                .await()

            if (snapshot.isEmpty) {
                // No conflicting reservation, proceed to add the reservation
                val newReservationRef = reservationsCollection.document()

                // Set reservation with a merge option to handle partial data updates
                newReservationRef.set(reservation, SetOptions.merge()).await()
                Log.d("ReservationsRepository", "Reservation added successfully.")
                Result.success(true)
            } else {
                Log.w("ReservationsRepository", "Conflict detected, another reservation was made.")
                Result.success(false)
            }
        }
    } catch (e: Exception) {
        Log.e("ReservationsRepository", "Error adding reservation: ${e.message}")
        Result.failure(e)
    }

    // Helper function to check for overlapping reservations
    private suspend fun checkForOverlap(reservation: Reservation): Boolean {
        return try {
            val existingReservations = reservationsCollection
                .whereEqualTo("parkingSlotID", reservation.parkingSlotID)
                .get()
                .await()
                .toObjects(Reservation::class.java)

            existingReservations.any { existingReservation ->
                isOverlapping(
                    reservation.reservationStart,
                    reservation.reservationEnd,
                    existingReservation.reservationStart,
                    existingReservation.reservationEnd
                )
            }
        } catch (e: Exception) {
            Log.e("ReservationsRepository", "Error checking for overlap: ${e.message}")
            false
        }
    }

    // Helper function to check if two date ranges overlap
    private fun isOverlapping(start1: Date, end1: Date, start2: Date, end2: Date): Boolean {
        return (start1 <= end2 && end1 >= start2)
    }

    // Removes the specified reservation from the system
    suspend fun deleteReservation(reservationID: String): Result<Boolean> {
        return try {
            Log.d("ReservationsRepository", "Attempting to delete reservation with ID: $reservationID")
            reservationsCollection.document(reservationID).delete().await()
            Log.d("ReservationsRepository", "Reservation deleted successfully for ID: $reservationID")
            Result.success(true)
        } catch (e: Exception) {
            Log.e("ReservationsRepository", "Error deleting reservation: ${e.message}")
            Result.failure(e)
        }
    }
}