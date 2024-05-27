package com.parkingmanagerapp.model

data class User(
    var uid: String = "", // Managed by Firebase Auth
    var name: String = "", // Stored in Firebase Auth as "displayname" attribute
    var surname: String = "", // Stored in Firestore
    var phoneNumber: String = "", // Managed by Firebase Auth
    var email: String = "", // Managed by Firebase Auth
    var role: UserRole = UserRole.REGULAR // Stored in Firestore
)