package com.parkingmanagerapp.utility

import com.google.firebase.firestore.Transaction

class FakeTransactionFunction(
    private val result: Boolean
) : Transaction.Function<Boolean> {
    override fun apply(transaction: Transaction): Boolean = result
}