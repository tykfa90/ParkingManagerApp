package com.parkingmanagerapp.model

data class User(
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String,
    val role: UserRole,
    val reservations: List<Reservation>
) {
    fun isAdmin() = role == UserRole.ADMIN

    companion object {
        fun fromFirebaseUser(firebaseUser: com.google.firebase.auth.FirebaseUser, role: UserRole = UserRole.REGULAR): User {
            return User(
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