package com.parkingmanagerapp.model

data class User(
    var uID: String = "",
    var name: String = "",
    var surname: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var role: UserRole = UserRole.REGULAR
) {
    companion object {
        fun fromFirebaseUser(firebaseUser: com.google.firebase.auth.FirebaseUser, role: UserRole): User {
            return User(
                uID = firebaseUser.uid,
                name = firebaseUser.displayName ?: "",
                surname = "",
                phoneNumber = firebaseUser.phoneNumber ?: "",
                email = firebaseUser.email ?: "",
                role = role
            )
        }
    }
}