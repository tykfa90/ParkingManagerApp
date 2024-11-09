package com.parkingmanagerapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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

    // Adds a new reservation using optimistic concurrency control
    suspend fun addReservation(reservation: Reservation): Result<Boolean> = try {
        // Perform the overlap check before starting the transaction
        val hasOverlap = checkForOverlap(reservation)

        if (hasOverlap) {
            Result.success(false) // The slot is already reserved
        } else {
            // Retrieve a snapshot of the reservations to check for concurrency issues
            val snapshot = reservationsCollection
                .whereEqualTo("parkingSlotID", reservation.parkingSlotID)
                .whereEqualTo("reservationStart", reservation.reservationStart)
                .get()
                .await()

            if (snapshot.isEmpty) {
                // No conflicting reservation, proceed to add the reservation
                val newReservationRef = reservationsCollection.document()

                // Set reservation with a merge option to handle partial data update
                newReservationRef.set(reservation, SetOptions.merge()).await()
                Result.success(true)
            } else {
                // Conflict detected, another reservation was made
                Result.success(false)
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun checkForOverlap(reservation: Reservation): Boolean {
        val existingReservations = reservationsCollection
            .whereEqualTo("parkingSlotID", reservation.parkingSlotID)
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

    private fun isOverlapping(start1: Date, end1: Date, start2: Date, end2: Date): Boolean {
        return (start1 <= end2 && end1 >= start2)
    }

    // Removes the specified reservation from the system using OCC
    suspend fun deleteReservation(reservationID: String): Result<Boolean> = try {
        val documentRef = reservationsCollection.document(reservationID)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(documentRef)
            if (snapshot.exists()) {
                // If the document has not changed, proceed to delete
                transaction.delete(documentRef)
            } else {
                throw Exception("Reservation already deleted or modified")
            }
        }.await()

        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }
}