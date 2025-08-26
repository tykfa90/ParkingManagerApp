package com.parkingmanagerapp.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.parkingmanagerapp.model.Reservation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetReservationsTest : RepositoryTestBase() {

    @Test
    fun `getUserReservations returns list of reservations`() = runTest(dispatcher) {
        val userId = "user1"
        val reservation = Reservation(
            reservationID = "r1",
            userID = userId,
            parkingSlotID = "slot1",
            licensePlate = "KR12345",
            reservationStart = Date(),
            reservationEnd = Date()
        )

        val document = mock<DocumentSnapshot> {
            on { toObject(Reservation::class.java) } doReturn reservation
        }

        val snapshot = mock<QuerySnapshot> {
            on { documents } doReturn listOf(document)
        }

        val query = mock<Query> {
            on { get() } doReturn Tasks.forResult(snapshot)
        }

        whenever(collection.whereEqualTo("userID", userId)).thenReturn(query)

        val result = repository.getUserReservations(userId)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
        assertEquals("r1", result.getOrThrow().first().reservationID)
    }

    @Test
    fun `getUserReservations returns failure on exception`() = runTest(dispatcher) {
        val userId = "userX"

        val query = mock<Query> {
            on { get() } doReturn Tasks.forException(RuntimeException("Firestore error"))
        }

        whenever(collection.whereEqualTo("userID", userId)).thenReturn(query)

        val result = repository.getUserReservations(userId)

        assertTrue(result.isFailure)
        assertEquals("Firestore error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getAllReservations returns reservations successfully`() = runTest(dispatcher) {
        val reservation = Reservation(
            reservationID = "r1",
            userID = "user1",
            parkingSlotID = "slot1",
            licensePlate = "ABC123",
            reservationStart = Date(),
            reservationEnd = Date()
        )

        val document = mock<DocumentSnapshot> {
            on { toObject(Reservation::class.java) } doReturn reservation
        }

        val snapshot = mock<QuerySnapshot> {
            on { documents } doReturn listOf(document)
        }

        whenever(collection.get()).thenReturn(Tasks.forResult(snapshot))

        val result = repository.getAllReservations()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
        assertEquals("r1", result.getOrThrow().first().reservationID)
    }

    @Test
    fun `getAllReservations returns failure on exception`() = runTest(dispatcher) {
        whenever(collection.get()).thenReturn(Tasks.forException(RuntimeException("Unexpected error")))

        val result = repository.getAllReservations()

        assertTrue(result.isFailure)
        assertEquals("Unexpected error", result.exceptionOrNull()?.message)
    }
}