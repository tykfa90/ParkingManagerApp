package com.parkingmanagerapp.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import com.parkingmanagerapp.model.Reservation
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date
import kotlin.coroutines.CoroutineContext
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ReservationsRepositoryTest {

    private lateinit var repository: ReservationsRepository
    private lateinit var firestore: FirebaseFirestore
    private lateinit var collection: CollectionReference
    private val dispatcher: CoroutineContext = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        firestore = mockk()
        collection = mockk()
        every { firestore.collection("reservations") } returns collection
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

        val querySnapshot = mockk<QuerySnapshot> {
            every { toObjects(Reservation::class.java) } returns emptyList()
        }

        coEvery {
            collection.whereEqualTo("parkingSlotID", reservation.parkingSlotID).get().await()
        } returns querySnapshot

        coEvery {
            firestore.runTransaction<Boolean>(any())
        } coAnswers {
            val block = arg<suspend (Transaction) -> Boolean>(0)
            val transaction = mockk<Transaction>(relaxed = true)
            val result = runBlocking { block(transaction) }
            Tasks.forResult(result)
        }

        val result = repository.addReservation(reservation)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow())
    }

    @Test
    fun `addReservation returns false when overlapping reservation exists`() = runTest(dispatcher) {
        val existingReservation = Reservation(
            reservationID = "res2",
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

        val querySnapshot = mockk<QuerySnapshot> {
            every { toObjects(Reservation::class.java) } returns listOf(existingReservation)
        }

        coEvery {
            collection.whereEqualTo("parkingSlotID", newReservation.parkingSlotID).get().await()
        } returns querySnapshot

        coEvery {
            firestore.runTransaction<Boolean>(any())
        } coAnswers {
            val transactionBlock = arg<suspend (Transaction) -> Boolean>(0)
            val transaction = mockk<Transaction>(relaxed = true)
            val result = runBlocking {
                transactionBlock(transaction)
            }
            Tasks.forResult(result)
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

        coEvery {
            collection.whereEqualTo("parkingSlotID", reservation.parkingSlotID).get().await()
        } throws RuntimeException("Firestore error")

        val result = repository.addReservation(reservation)

        assertTrue(result.isFailure)
        assertEquals("Firestore error", result.exceptionOrNull()?.message)
    }
}