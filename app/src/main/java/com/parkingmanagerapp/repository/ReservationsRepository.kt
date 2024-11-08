package com.parkingmanagerapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.parkingmanagerapp.model.Reservation
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val reservationsCollection = firestore.collection("reservations")

    // Fetches reservations by userID
    suspend fun getUserReservations(userID: String): Result<List<Reservation>> = try {
        val snapshot = reservationsCollection
            .whereEqualTo("userID", userID)
            .get()
            .await()
        Result.success(snapshot.toObjects(Reservation::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Fetches all existing reservations
    suspend fun getAllReservations(): Result<List<Reservation>> = try {
        val snapshot = reservationsCollection.get().await()
        Result.success(snapshot.documents.mapNotNull { it.toObject(Reservation::class.java) })
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Adds a new reservation after performing an overlap check
    suspend fun addReservation(reservation: Reservation): Result<Boolean> = try {
        // Check for overlapping reservations before starting a transaction
        val hasOverlap = checkForOverlap(reservation)

        if (hasOverlap) {
            Result.success(false) // Indicate that the slot is already reserved
        } else {
            // Proceed with the transaction to add the reservation
            firestore.runTransaction { transaction ->
                val newReservationRef = reservationsCollection.document()
                transaction.set(newReservationRef, reservation)
            }.await()
            Result.success(true) // Reservation successfully added
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Helper function to verify whether there is no overlap existing when creating a new reservation
    private suspend fun checkForOverlap(reservation: Reservation): Boolean {
        val existingReservations = reservationsCollection
            .whereEqualTo("parkingSlotID", reservation.parkingSpaceID)
            .get()
            .await()
            .toObjects(Reservation::class.java)

        return existingReservations.any { existingReservation ->
            isOverlapping(
                reservation.reservationStart,
                reservation.reservationEnd,
                existingReservation.reservationStart,
                existingReservation.reservationEnd
            )
        }
    }

    // Helper function to check if two date ranges overlap
    private fun isOverlapping(start1: Date, end1: Date, start2: Date, end2: Date): Boolean {
        return (start1 <= end2 && end1 >= start2)
    }

    // Removes the specified reservation from the system
    suspend fun deleteReservation(reservationID: String): Result<Boolean> = try {
        reservationsCollection.document(reservationID).delete().await()
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }
}