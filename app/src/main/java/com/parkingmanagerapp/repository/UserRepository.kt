package com.parkingmanagerapp.repository

import android.util.Log
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

    // Signs-in a user and fetch their details
    suspend fun signInUser(email: String, password: String): User? = withContext(ioDispatcher) {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return@withContext null
            val userId = firebaseUser.uid
            val docSnapshot = db.collection("users").document(userId).get().await()
            val role = UserRole.valueOf(docSnapshot.getString("role") ?: "REGULAR")
            User.fromFirebaseUser(firebaseUser, role)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error while signing in user: ${e.localizedMessage}")
            null
        }
    }

    // Registers a new user, sets their role to "regular", and stores user details
    suspend fun registerNewUser(user: User, password: String): Boolean = withContext(ioDispatcher) {
        try {
            val result = auth.createUserWithEmailAndPassword(user.email, password).await()
            val firebaseUser = result.user ?: return@withContext false

            // Assigns the UID from the newly created Firebase user to your User object
            val newUser = user.copy(uid = firebaseUser.uid)

            // Updates the database with the User object that now includes the UID
            db.collection("users").document(newUser.uid)
                .set(newUser.toMap()).await()
            true
        } catch (e: Exception) {
            // Logs the error for more clear debug
            Log.e("UserRepository", "Error while registering new user: ${e.localizedMessage}")
            false
        }
    }

    // Helper method for converting User object into Firestore-friendly format
    private fun User.toMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "surname" to surname,
            "phoneNumber" to phoneNumber,
            "email" to email,
            "role" to role.toString()
        )
    }

    // Signs-out the current user
    suspend fun signOutUser() = withContext(ioDispatcher) {
        auth.signOut()
    }

    // Reads currently active user information from database
    suspend fun getCurrentUser(): User? = withContext(ioDispatcher) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return@withContext null
        try {
            // Attempts to fetch the user document from Firestore using the UID
            val docSnapshot = db.collection("users").document(firebaseUser.uid).get().await()
            val role = UserRole.valueOf(docSnapshot.getString("role") ?: "REGULAR")

            // Creates and return the User object with details fetched from Firestore
            return@withContext User(
                uid = firebaseUser.uid,
                name = docSnapshot.getString("name") ?: "",
                surname = docSnapshot.getString("surname") ?: "",
                phoneNumber = firebaseUser.phoneNumber ?: "",
                email = firebaseUser.email ?: "",
                role = role
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("UserRepository", "Error while fetching current user: ${e.localizedMessage}")
            return@withContext null
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
            Log.e("UserRepository", "Error while updating user details: ${e.localizedMessage}")
            false
        }
    }

    // Updates the user role (admin level only)
    suspend fun updateUserRole(userId: String, newRole: UserRole): Boolean = withContext(ioDispatcher) {
        try {
            db.collection("users").document(userId)
                .update("role", newRole.toString()).await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error while updating user role: ${e.localizedMessage}")
            false
        }
    }

    // Fetches all users from Firestore
    suspend fun getAllUsers(): List<User> = withContext(ioDispatcher) {
        try {
            val usersSnapshot = db.collection("users").get().await()
            usersSnapshot.documents.mapNotNull { it.toObject(User::class.java) }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error while fetching all user accounts: ${e.localizedMessage}")
            emptyList()
        }
    }

    // Deletes the user from database
    suspend fun deleteUser(userId: String): Boolean = withContext(ioDispatcher) {
        try {
            db.collection("users").document(userId).delete().await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error while deleting user account: ${e.localizedMessage}")
            false
        }
    }
}