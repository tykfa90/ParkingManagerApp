package com.parkingmanagerapp.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
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

    // Signs-in the user using Firebase Auth service
    suspend fun signInUser(email: String, password: String): User? = withContext(ioDispatcher) {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return@withContext null
            val userId = firebaseUser.uid
            val docSnapshot = db.collection("users").document(userId).get().await()
            val role = UserRole.valueOf(docSnapshot.getString("role") ?: "REGULAR")
            User(
                uid = userId,
                name = firebaseUser.displayName ?: "",
                surname = docSnapshot.getString("surname") ?: "",
                phoneNumber = firebaseUser.phoneNumber ?: "",
                email = firebaseUser.email ?: "",
                role = role
            )
        } catch (e: Exception) {
            Log.e("UserRepository", "Error while signing in user: ${e.localizedMessage}")
            null
        }
    }

    // Registers new user in Firebase Auth, then stores additional information in Firebase
    suspend fun registerNewUser(user: User, password: String): Boolean = withContext(ioDispatcher) {
        try {
            val result = auth.createUserWithEmailAndPassword(user.email, password).await()
            val firebaseUser = result.user ?: return@withContext false

            val profileUpdates = userProfileChangeRequest {
                displayName = user.name
            }
            firebaseUser.updateProfile(profileUpdates).await()

            val newUser = user.copy(uid = firebaseUser.uid)

            // Save additional attributes to Firestore
            db.collection("users").document(newUser.uid).set(newUser.toMap()).await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error while registering new user: ${e.localizedMessage}")
            false
        }
    }

    private fun User.toMap(): Map<String, Any> {
        return mapOf(
            "surname" to surname,
            "role" to role.toString(),
            "phoneNumber" to phoneNumber
        )
    }

    // Signs-out the active user
    suspend fun signOutUser() = withContext(ioDispatcher) {
        auth.signOut()
    }

    // Fetches a user by the uid
    suspend fun getCurrentUser(): User? = withContext(ioDispatcher) {
        val firebaseUser = auth.currentUser ?: return@withContext null
        try {
            val docSnapshot = db.collection("users").document(firebaseUser.uid).get().await()
            val role = UserRole.valueOf(docSnapshot.getString("role") ?: "REGULAR")
            User(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName ?: "",
                surname = docSnapshot.getString("surname") ?: "",
                phoneNumber = firebaseUser.phoneNumber ?: "",
                email = firebaseUser.email ?: "",
                role = role
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("UserRepository", "Error while fetching current user: ${e.localizedMessage}")
            null
        }
    }

    // Updates user's account details
    suspend fun updateUserDetails(user: User): Boolean = withContext(ioDispatcher) {
        try {
            // Update additional attributes in Firestore
            db.collection("users").document(user.uid)
                .update(
                    "surname", user.surname,
                    "role", user.role.toString(),
                    "phoneNumber", user.phoneNumber
                ).await()

            // Update Firebase Auth user attributes
            val firebaseUser = auth.currentUser
            val profileUpdates = userProfileChangeRequest {
                displayName = user.name
            }
            firebaseUser?.updateProfile(profileUpdates)?.await()
            firebaseUser?.updateEmail(user.email)?.await()
            TODO("Implement re-authentication required to update the phone number.")
            // firebaseUser?.updatePhoneNumber(user.phoneNumber)?.await()

            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error while updating user details: ${e.localizedMessage}")
            false
        }
    }

    // Fetches a list of all registered users
    suspend fun getAllUsers(): List<User> = withContext(ioDispatcher) {
        try {
            val usersSnapshot = db.collection("users").get().await()
            usersSnapshot.documents.mapNotNull { doc ->
                val user = doc.toObject(User::class.java)
                user?.apply { uid = doc.id }
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error while fetching all user accounts: ${e.localizedMessage}")
            emptyList()
        }
    }

    // Deletes a specified user account entirely
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