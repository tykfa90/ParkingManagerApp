package com.parkingmanagerapp.model

data class User(
    val uid: String,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String,
    val role: UserRole,
    val reservations: List<Reservation>
) {
    companion object {
        fun fromFirebaseUser(firebaseUser: com.google.firebase.auth.FirebaseUser, role: UserRole = UserRole.REGULAR): User {
            return User(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName ?: "",
                surname = "",
                phoneNumber = firebaseUser.phoneNumber ?: "",
                email = firebaseUser.email ?: "",
                role = role,
                reservations = emptyList()
            )
        }
    }
}