package com.parkingmanagerapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.parkingmanagerapp.model.Reservation
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getUserReservations(userID: String): List<Reservation> {
        val snapshot = firestore.collection("reservations")
            .whereEqualTo("userID", userID)
            .get()
            .await()
        return snapshot.toObjects(Reservation::class.java)
    }

    suspend fun getAllReservations(): List<Reservation> {
        val snapshot = firestore.collection("reservations").get().await()
        return snapshot.documents.mapNotNull { it.toObject(Reservation::class.java) }
    }

    suspend fun addReservation(reservation: Reservation) {
        firestore.collection("reservations").add(reservation).await()
    }

    suspend fun deleteReservation(reservationID: String) {
        firestore.collection("reservations").document(reservationID).delete().await()
    }
}