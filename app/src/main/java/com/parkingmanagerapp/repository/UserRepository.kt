package com.parkingmanagerapp.repository

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
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

    /**
     * Signs-in the user using Firebase Auth service.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     * @return The user object if sign-in is successful, null otherwise.
     */
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

    /**
     * Registers a new user in Firebase Auth, then stores additional information in Firestore.
     *
     * @param user The user object containing the user's information.
     * @param password The password for the new user.
     * @return True if registration is successful, false otherwise.
     */
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

    /**
     * Converts the User object to a Map for Firestore storage.
     *
     * @return A map containing the user's information.
     */
    private fun User.toMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "surname" to surname,
            "phoneNumber" to phoneNumber,
            "email" to email,
            "role" to role.toString()
        )
    }

    /**
     * Signs out the currently authenticated user.
     */
    suspend fun signOutUser() = withContext(ioDispatcher) {
        auth.signOut()
    }

    /**
     * Fetches the currently authenticated user.
     *
     * @return The current user object if authenticated, null otherwise.
     */
    suspend fun getCurrentUser(): User? = withContext(ioDispatcher) {
        val firebaseUser = auth.currentUser ?: return@withContext null
        try {
            val docSnapshot = db.collection("users").document(firebaseUser.uid).get().await()
            User(
                uid = firebaseUser.uid,
                name = docSnapshot.getString("name") ?: firebaseUser.displayName ?: "",
                surname = docSnapshot.getString("surname") ?: "",
                phoneNumber = docSnapshot.getString("phoneNumber") ?: "",
                email = docSnapshot.getString("email") ?: firebaseUser.email ?: "",
                role = UserRole.valueOf(docSnapshot.getString("role") ?: "REGULAR")
            )
        } catch (e: Exception) {
            Log.e("UserRepository", "Error fetching user data: ${e.localizedMessage}")
            null
        }
    }

    /**
     * Updates the user's first name in both Firebase Auth and Firestore.
     *
     * @param user The user object containing the updated first name.
     * @return True if the update is successful, false otherwise.
     */
    suspend fun updateUserFirstName(user: User): Boolean = withContext(ioDispatcher) {
        try {
            // Update Firestore
            db.collection("users").document(user.uid).update("name", user.name).await()

            // Update Firebase Auth display name
            val profileUpdates = userProfileChangeRequest {
                displayName = user.name
            }
            auth.currentUser?.updateProfile(profileUpdates)?.await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error updating user name: ${e.localizedMessage}")
            false
        }
    }

    /**
     * Updates the user's surname in Firestore.
     *
     * @param user The user object containing the updated surname.
     * @return True if the update is successful, false otherwise.
     */
    suspend fun updateUserSurname(user: User): Boolean = withContext(ioDispatcher) {
        try {
            db.collection("users").document(user.uid).update("surname", user.surname).await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error updating user surname: ${e.localizedMessage}")
            false
        }
    }

    /**
     * Re-authenticates the user with the provided email and password.
     *
     * @param email The user's email address.
     * @param password The user's current password.
     * @return True if re-authentication is successful, false otherwise.
     */
    suspend fun reauthenticateUser(email: String, password: String): Boolean =
        withContext(ioDispatcher) {
            try {
                val user = auth.currentUser ?: return@withContext false
                val credential = EmailAuthProvider.getCredential(email, password)
                user.reauthenticate(credential).await()
                true
            } catch (e: Exception) {
                Log.e("UserRepository", "Error re-authenticating user: ${e.localizedMessage}")
                false
            }
        }

    /**
     * Updates the user's email address in both Firebase Auth and Firestore.
     *
     * @param newEmail The new email address.
     * @return True if the update is successful, false otherwise.
     */
    suspend fun updateUserEmail(newEmail: String): Boolean = withContext(ioDispatcher) {
        try {
            val user = auth.currentUser ?: return@withContext false
            user.updateEmail(newEmail).await()
            db.collection("users").document(user.uid).update("email", newEmail).await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error updating email: ${e.localizedMessage}")
            false
        }
    }

    /**
     * Updates the user's password in Firebase Auth.
     *
     * @param newPassword The new password.
     * @return True if the update is successful, false otherwise.
     */
    suspend fun updateUserPassword(newPassword: String): Boolean = withContext(ioDispatcher) {
        try {
            val user = auth.currentUser ?: return@withContext false
            user.updatePassword(newPassword).await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error updating password: ${e.localizedMessage}")
            false
        }
    }

    /**
     * Updates the user's phone number in both Firebase Auth and Firestore.
     *
     * @param newPhoneNumber The new phone number.
     * @param verificationId The verification ID from the phone number verification process.
     * @param code The verification code sent to the new phone number.
     * @return True if the update is successful, false otherwise.
     */
    suspend fun updateUserPhoneNumber(
        newPhoneNumber: String,
        verificationId: String,
        code: String
    ): Boolean = withContext(ioDispatcher) {
        try {
            val user = auth.currentUser ?: return@withContext false
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            user.updatePhoneNumber(credential).await()
            db.collection("users").document(user.uid).update("phoneNumber", newPhoneNumber).await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error updating phone number: ${e.localizedMessage}")
            false
        }
    }

    /**
     * Fetches all users as a list.
     *
     * @return A list of all user objects.
     */
    suspend fun getAllUsers(): List<User> = withContext(ioDispatcher) {
        try {
            val usersSnapshot = db.collection("users").get().await()
            usersSnapshot.documents.mapNotNull { doc ->
                val userId = doc.id
                User(
                    uid = userId,
                    name = doc.getString("name") ?: "",
                    surname = doc.getString("surname") ?: "",
                    phoneNumber = doc.getString("phoneNumber") ?: "",
                    email = doc.getString("email") ?: "",
                    role = UserRole.valueOf(doc.getString("role") ?: "REGULAR")
                )
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error while fetching all user accounts: ${e.localizedMessage}")
            emptyList()
        }
    }

    /**
     * Deletes a specified user account entirely from Firestore.
     *
     * @param userId The user ID of the account to be deleted.
     * @return True if the deletion is successful, false otherwise.
     */
    suspend fun deleteUser(userId: String): Boolean = withContext(ioDispatcher) {
        try {
            db.collection("users").document(userId).delete().await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error while deleting user: ${e.localizedMessage}")
            false
        }
    }
}