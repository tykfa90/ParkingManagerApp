package com.parkingmanagerapp.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.parkingmanagerapp.model.User
import com.parkingmanagerapp.model.UserRole
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineContext
) {

    // Function to sign in a user and fetch their details
    suspend fun signInUser(email: String, password: String): User? = withContext(ioDispatcher) {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return@withContext null
            val userId = firebaseUser.uid
            val docSnapshot = db.collection("users").document(userId).get().await()
            val role = UserRole.valueOf(docSnapshot.getString("role") ?: "REGULAR")
            User.fromFirebaseUser(firebaseUser, role)
        } catch (e: Exception) {
            null
        }
    }

    // Function to sign up a new user, set their role to "regular", and store user details
    suspend fun signUpUser(user: User, password: String): Boolean = withContext(ioDispatcher) {
        try {
            val result = auth.createUserWithEmailAndPassword(user.email, password).await()
            val firebaseUser = result.user ?: return@withContext false
            val newUser = User.fromFirebaseUser(firebaseUser, UserRole.REGULAR)
            db.collection("users").document(firebaseUser.uid)
                .set(newUser.toMap()).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Additional method in User class to convert it to a map for Firestore
    private fun User.toMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "surname" to surname,
            "phoneNumber" to phoneNumber,
            "email" to email,
            "role" to role.toString(),
            "reservations" to reservations // Assuming you have a way to convert reservations to a Firestore-friendly format
        )
    }

    // Function to fetch the role of the current user
    suspend fun fetchUserRole(userId: String): UserRole? = withContext(ioDispatcher) {
        try {
            val docSnapshot = db.collection("users").document(userId).get().await()
            UserRole.valueOf(docSnapshot.getString("role") ?: "REGULAR")
        } catch (e: Exception) {
            null
        }
    }

    // Function to update user role (admin use only)
    suspend fun updateUserRole(userId: String, newRole: UserRole): Boolean = withContext(ioDispatcher) {
        try {
            db.collection("users").document(userId)
                .update("role", newRole.toString()).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}