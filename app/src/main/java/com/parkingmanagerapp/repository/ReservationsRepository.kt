package com.parkingmanagerapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.parkingmanagerapp.model.ParkingSlot
import com.parkingmanagerapp.model.Reservation
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class ReservationsRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineContext
) {
    private val reservationsCollection = db.collection("reservations")
    private val parkingSlotsCollection = db.collection("parkingSlots")

    suspend fun createReservation(reservation: Reservation): Boolean = withContext(ioDispatcher) {
        return@withContext try {
            reservationsCollection.document(reservation.reservationID).set(reservation).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Fetches available parking slots within the specified date range
    suspend fun getAvailableSlots(startDate: Date, endDate: Date): List<ParkingSlot> =
        withContext(ioDispatcher) {
            return@withContext try {
                // Fetches all reservations that overlap with the specified date range
                val reservedSlotsSnapshot = reservationsCollection
                    .whereLessThan("reservationEnd", endDate)
                    .whereGreaterThan("reservationStart", startDate)
                    .get().await()

                val reservedSlotIDs =
                    reservedSlotsSnapshot.documents.map { it.getString("parkingSlotID") }

                // Fetches all parking slots and excludes those that are reserved
                val parkingSlotsSnapshot = parkingSlotsCollection.get().await()
                val allParkingSlots = parkingSlotsSnapshot.toObjects(ParkingSlot::class.java)

                allParkingSlots.filter { it.parkingSlotID !in reservedSlotIDs }
            } catch (e: Exception) {
                emptyList()
            }
        }
}