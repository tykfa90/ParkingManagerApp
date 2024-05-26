package com.parkingmanagerapp.model

data class User(
    var uid: String,
    var name: String,
    var surname: String,
    var phoneNumber: String,
    var email: String,
    var role: UserRole
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