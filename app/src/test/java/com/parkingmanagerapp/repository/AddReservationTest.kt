package com.parkingmanagerapp.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import com.parkingmanagerapp.model.Reservation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AddReservationTest : RepositoryTestBase() {

    @BeforeEach
    fun setUp() {
        firestore = mock()
        collection = mock()
        whenever(firestore.collection("reservations")).thenReturn(collection)
        repository = ReservationsRepository(firestore, dispatcher)
    }

    @Test
    fun `addReservation returns success when no overlaps`() = runTest(dispatcher) {
        val reservation = Reservation(
            reservationID = "res1",
            userID = "user1",
            parkingSlotID = "slot1",
            licensePlate = "XYZ123",
            reservationStart = Date(1000000),
            reservationEnd = Date(2000000)
        )

        val snapshot = mock<QuerySnapshot> {
            on { toObjects(Reservation::class.java) } doReturn emptyList()
        }

        val query = mock<Query> {
            on { get() } doReturn Tasks.forResult(snapshot)
        }

        whenever(collection.whereEqualTo("parkingSlotID", "slot1")) doReturn query

        whenever(firestore.runTransaction(any<Transaction.Function<Boolean>>()))
            .thenAnswer { invocation ->
                val function = invocation.getArgument<Transaction.Function<Boolean>>(0)
                Tasks.forResult(function.apply(mock()))
            }

        val result = repository.addReservation(reservation)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow())
    }

    @Test
    fun `addReservation returns false when overlapping reservation exists`() = runTest(dispatcher) {
        val overlapping = Reservation(
            reservationID = "existing",
            userID = "user2",
            parkingSlotID = "slot1",
            licensePlate = "ABC123",
            reservationStart = Date(1500000),
            reservationEnd = Date(2500000)
        )

        val newReservation = Reservation(
            reservationID = "res3",
            userID = "user3",
            parkingSlotID = "slot1",
            licensePlate = "XYZ789",
            reservationStart = Date(1000000),
            reservationEnd = Date(2000000)
        )

        val snapshot = mock<QuerySnapshot> {
            on { toObjects(Reservation::class.java) } doReturn listOf(overlapping)
        }

        val query = mock<Query> {
            on { get() } doReturn Tasks.forResult(snapshot)
        }

        whenever(collection.whereEqualTo("parkingSlotID", "slot1")) doReturn query

        whenever(firestore.runTransaction(any<Transaction.Function<Boolean>>()))
            .thenAnswer { invocation ->
                val function = invocation.getArgument<Transaction.Function<Boolean>>(0)
                Tasks.forResult(function.apply(mock()))
            }

        val result = repository.addReservation(newReservation)

        assertTrue(result.isSuccess)
        assertEquals(false, result.getOrNull())
    }

    @Test
    fun `addReservation returns failure on exception`() = runTest(dispatcher) {
        val reservation = Reservation(
            reservationID = "res4",
            userID = "user4",
            parkingSlotID = "slot1",
            licensePlate = "ERR456",
            reservationStart = Date(),
            reservationEnd = Date()
        )

        val query = mock<Query> {
            on { get() } doReturn Tasks.forException(RuntimeException("Firestore error"))
        }

        whenever(collection.whereEqualTo("parkingSlotID", "slot1")) doReturn query

        val result = repository.addReservation(reservation)

        assertTrue(result.isFailure)
        assertEquals("Firestore error", result.exceptionOrNull()?.message)
    }
}