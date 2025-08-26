package com.parkingmanagerapp.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.coroutines.CoroutineContext

abstract class RepositoryTestBase {
    protected lateinit var firestore: FirebaseFirestore
    protected lateinit var collection: CollectionReference
    protected lateinit var repository: ReservationsRepository
    protected val dispatcher: CoroutineContext = UnconfinedTestDispatcher()
}