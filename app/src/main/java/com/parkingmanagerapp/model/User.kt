package com.parkingmanagerapp.model

data class User(
    var uid: String,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String,
    val role: UserRole
) {
    companion object {
        fun fromFirebaseUser(firebaseUser: com.google.firebase.auth.FirebaseUser, role: UserRole = UserRole.REGULAR): User {
            return User(
                uid = firebaseUser.uid,
                name = "",
                surname = "",
                phoneNumber = firebaseUser.phoneNumber ?: "",
                email = firebaseUser.email ?: "",
                role = role
            )
        }
    }
}