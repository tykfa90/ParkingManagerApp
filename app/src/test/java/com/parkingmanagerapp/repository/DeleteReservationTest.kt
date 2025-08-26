package com.parkingmanagerapp.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteReservationTest : RepositoryTestBase() {

    @Test
    fun `deleteReservation returns success when reservation exists`() = runTest(dispatcher) {
        val reservationId = "res1"
        val firestoreDocId = "doc123"

        val document = mock<DocumentSnapshot> {
            on { id } doReturn firestoreDocId
        }

        val snapshot = mock<QuerySnapshot> {
            on { isEmpty } doReturn false
            on { documents } doReturn listOf(document)
        }

        val query = mock<Query> {
            on { get() } doReturn Tasks.forResult(snapshot)
        }

        whenever(collection.whereEqualTo("reservationID", reservationId)).thenReturn(query)
        whenever(collection.document(firestoreDocId).delete()).thenReturn(Tasks.forResult(null))

        val result = repository.deleteReservation(reservationId)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow())
    }

    @Test
    fun `deleteReservation returns failure when reservation not found`() = runTest(dispatcher) {
        val reservationId = "resX"

        val snapshot = mock<QuerySnapshot> {
            on { isEmpty } doReturn true
        }

        val query = mock<Query> {
            on { get() } doReturn Tasks.forResult(snapshot)
        }

        whenever(collection.whereEqualTo("reservationID", reservationId)).thenReturn(query)

        val result = repository.deleteReservation(reservationId)

        assertTrue(result.isFailure)
        assertEquals("Reservation not found", result.exceptionOrNull()?.message)
    }

    @Test
    fun `deleteReservation returns failure on Firestore error`() = runTest(dispatcher) {
        val reservationId = "resX"

        val query = mock<Query> {
            on { get() } doReturn Tasks.forException(RuntimeException("Delete error"))
        }

        whenever(collection.whereEqualTo("reservationID", reservationId)).thenReturn(query)

        val result = repository.deleteReservation(reservationId)

        assertTrue(result.isFailure)
        assertEquals("Delete error", result.exceptionOrNull()?.message)
    }
}