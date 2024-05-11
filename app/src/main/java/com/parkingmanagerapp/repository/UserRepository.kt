package com.parkingmanagerapp.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.parkingmanagerapp.model.Reservation
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
            Log.e("UserRepository", "Error signing in user: ${e.localizedMessage}")
            null
        }
    }

    // Function to sign up a new user, set their role to "regular", and store user details
    suspend fun registerNewUser(user: User, password: String): Boolean = withContext(ioDispatcher) {
        try {
            val result = auth.createUserWithEmailAndPassword(user.email, password).await()
            val firebaseUser = result.user ?: return@withContext false

            // Assign the UID from the newly created Firebase user to your User object
            val newUser = user.copy(uid = firebaseUser.uid)

            // Update the database with the User object that now includes the UID
            db.collection("users").document(newUser.uid)
                .set(newUser.toMap()).await()
            true
        } catch (e: Exception) {
            // Consider logging the error for better debuggability
            Log.e("UserRepository", "Registration failed: ${e.localizedMessage}")
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
            "role" to role.toString()
        )
    }

    // Function to sign-out the current user
    suspend fun signOutUser() = withContext(ioDispatcher) {
        auth.signOut()
    }

    // Reads currently active user information from database
    suspend fun getCurrentUser(): User? = withContext(ioDispatcher) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return@withContext null
        try {
            // Attempt to fetch the user document from Firestore using the UID
            val docSnapshot = db.collection("users").document(firebaseUser.uid).get().await()
            val role = UserRole.valueOf(docSnapshot.getString("role") ?: "REGULAR")

            // Create and return the User object with details fetched from Firestore
            return@withContext User(
                uid = firebaseUser.uid,
                name = docSnapshot.getString("name") ?: "",
                surname = docSnapshot.getString("surname") ?: "",
                phoneNumber = firebaseUser.phoneNumber ?: "",
                email = firebaseUser.email ?: "",
                role = role
            )
        } catch (e: Exception) {
            // Handle any errors that might occur during Firestore access
            e.printStackTrace()
            Log.e("UserRepository", "Error fetching current user: ${e.localizedMessage}")
            return@withContext null
        }
    }

    // Safely convert reservations list or supply an empty one
    private fun convertToReservationList(data: Any?): List<Reservation> {
        return if (data is List<*>) {
            @Suppress("UNCHECKED_CAST")
            val reservationList = data as? List<Reservation>

            reservationList ?: run {
                emptyList<Reservation>()
            }
        } else {
            // Handle the case where data is not a list
            emptyList()
        }
    }

    // Updates user data/details
    suspend fun updateUserDetails(user: User): Boolean = withContext(ioDispatcher) {
        try {
            db.collection("users").document(user.uid)
                .update(
                    "name", user.name,
                    "surname", user.surname,
                    "phoneNumber", user.phoneNumber,
                    "email", user.email
                ).await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error updating user details: ${e.localizedMessage}")
            false
        }
    }

    suspend fun updateUserRole(userId: String, newRole: UserRole): Boolean = withContext(ioDispatcher) {
        try {
            db.collection("users").document(userId)
                .update("role", newRole.toString()).await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error updating user role: ${e.localizedMessage}")
            false
        }
    }
}