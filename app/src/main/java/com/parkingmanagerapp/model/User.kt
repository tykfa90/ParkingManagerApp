package com.parkingmanagerapp.model

data class User(
    var uid: String = "", // Managed by Firebase Auth
    var name: String = "", // Stored in Firebase Auth as "displayname" attribute
    var email: String = "", // Managed by Firebase Auth
    var phoneNumber: String = "", // Managed by Firebase Auth

    var surname: String = "", // Stored in Firestore
    var role: UserRole = UserRole.REGULAR, // Stored in Firestore
    val active: Boolean // Flag to handle user's ability to access the account
)