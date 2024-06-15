package com.parkingmanagerapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.parkingmanagerapp.model.Reservation
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result

@Singleton
class ReservationsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getUserReservations(userID: String): Result<List<Reservation>> {
        return try {
            val snapshot = firestore.collection("reservations")
                .whereEqualTo("userID", userID)
                .get()
                .await()
            Result.success(snapshot.toObjects(Reservation::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllReservations(): Result<List<Reservation>> {
        return try {
            val snapshot = firestore.collection("reservations").get().await()
            Result.success(snapshot.toObjects(Reservation::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addReservation(reservation: Reservation): Result<Unit> {
        return try {
            firestore.collection("reservations").add(reservation).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReservation(reservationID: String): Result<Unit> {
        return try {
            firestore.collection("reservations").document(reservationID).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}