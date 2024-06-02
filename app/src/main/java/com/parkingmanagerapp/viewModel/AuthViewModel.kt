package com.parkingmanagerapp.viewModel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.parkingmanagerapp.model.User
import com.parkingmanagerapp.model.UserRole
import com.parkingmanagerapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _signInStatus = MutableStateFlow<Boolean?>(null)
    val signInStatus = _signInStatus.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    // Snackbar handling
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    // List of all users for admin management
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    private val _verificationId = MutableStateFlow<String?>(null)
    val verificationId = _verificationId.asStateFlow()

    private val _useTestPhoneNumber = MutableStateFlow<Boolean>(false)
    val useTestPhoneNumber = _useTestPhoneNumber.asStateFlow()

    init {
        // Checks if a user is logged in when ViewModel is created
        checkAuthenticationState()
        fetchAllUsers()
    }

    // Fetches all users for admin management
    fun fetchAllUsers() {
        viewModelScope.launch {
            try {
                val userList = userRepository.getAllUsers()
                _users.value = userList
            } catch (e: Exception) {
                _snackbarMessage.value = "Error fetching users: ${e.localizedMessage}"
            }
        }
    }

    // Sorts users by name
    fun sortUsersByFirstName() {
        _users.value = _users.value.sortedBy { it.name }
    }

    // Sorts users by surname
    fun sortUsersBySurname() {
        _users.value = _users.value.sortedBy { it.surname }
    }

    // Signs in the user, using the basic e-mail + password method
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.signInUser(email, password)
            if (result != null) {
                _user.value = result
                _signInStatus.value = true
                _snackbarMessage.value = "Sign in successful!"
            } else {
                _signInStatus.value = false
                _snackbarMessage.value = "Sign in failed"
            }
        }
    }

    // Registers a new user in the FirebaseAuth database. After generating uid,
    // passes over the newUser account with additional data to the Firebase Firestore
    fun registerWithEmailAndPassword(email: String, password: String, name: String) {
        viewModelScope.launch {
            val newUser = User(
                name = name,
                email = email,
                role = UserRole.REGULAR // Defaults all new accounts to REGULAR type account
            )

            val success = userRepository.registerNewUser(newUser, password)
            if (success) {
                // Upon successful registration, update the UI state and user flow
                _user.value =
                    newUser // Temporarily set user value
                _signInStatus.value = true
                _snackbarMessage.value = "Registration successful!"
            } else {
                _signInStatus.value = false
                _snackbarMessage.value = "Registration failed. Please try again."
            }
        }
    }

    // Signs-out the currently active user
    fun signOut() {
        viewModelScope.launch {
            userRepository.signOutUser()
            _user.value = null
            _signInStatus.value = null
            _snackbarMessage.value = "Signed out successfully."
        }
    }

    // Verifies whether there is an active user that is correctly authenticated within the application
    private fun checkAuthenticationState() {
        viewModelScope.launch {
            _user.value = userRepository.getCurrentUser()
            _signInStatus.value = _user.value != null
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

    fun setSnackbarMessage(message: String) {
        _snackbarMessage.value = message
    }

    // Deletes the user account
    fun deleteUser(userId: String) {
        viewModelScope.launch {
            if (userRepository.deleteUser(userId)) {
                fetchAllUsers()
                _snackbarMessage.value = "User deleted successfully."
            } else {
                _snackbarMessage.value = "Failed to delete user."
            }
        }
    }

    fun updateUserEmail(password: String, newEmail: String) {
        val currentUser = auth.currentUser ?: return

        viewModelScope.launch {
            if (userRepository.reauthenticateUser(currentUser.email!!, password)) {
                if (userRepository.updateUserEmail(newEmail)) {
                    fetchAllUsers() // Fetch users to update the state
                    _snackbarMessage.value = "Email updated successfully."
                } else {
                    _snackbarMessage.value = "Failed to update email."
                }
            } else {
                _snackbarMessage.value = "Re-authentication failed."
            }
        }
    }

    fun updateUserPassword(password: String, newPassword: String) {
        val currentUser = auth.currentUser ?: return

        viewModelScope.launch {
            if (userRepository.reauthenticateUser(currentUser.email!!, password)) {
                if (userRepository.updateUserPassword(newPassword)) {
                    _snackbarMessage.value = "Password updated successfully."
                } else {
                    _snackbarMessage.value = "Failed to update password."
                }
            } else {
                _snackbarMessage.value = "Re-authentication failed."
            }
        }
    }

    fun updateUserPhoneNumber(password: String, newPhoneNumber: String, verificationId: String, code: String) {
        val currentUser = auth.currentUser ?: return

        viewModelScope.launch {
            if (userRepository.reauthenticateUser(currentUser.email!!, password)) {
                if (_useTestPhoneNumber.value && newPhoneNumber == "+48 111111111" && code == "111111") {
                    if (userRepository.updateUserPhoneNumber(newPhoneNumber, verificationId, code)) {
                        _snackbarMessage.value = "Phone number updated successfully."
                    } else {
                        _snackbarMessage.value = "Failed to update phone number."
                    }
                } else {
                    try {
                        val credential = PhoneAuthProvider.getCredential(verificationId, code)
                        currentUser.updatePhoneNumber(credential).await()
                        userRepository.updateUserPhoneNumber(newPhoneNumber, verificationId, code)
                        _snackbarMessage.value = "Phone number updated successfully."
                    } catch (e: Exception) {
                        _snackbarMessage.value = "Failed to update phone number: ${e.localizedMessage}"
                    }
                }
            } else {
                _snackbarMessage.value = "Re-authentication failed."
            }
        }
    }

    fun setUseTestPhoneNumber(useTest: Boolean) {
        _useTestPhoneNumber.value = useTest
    }

    fun updateUserName(newName: String) {
        viewModelScope.launch {
            val currentUser = userRepository.getCurrentUser() ?: return@launch
            val updatedUser = currentUser.copy(name = newName)
            if (userRepository.updateUserFirstName(updatedUser)) {
                fetchAllUsers()
                _snackbarMessage.value = "Name updated successfully."
            } else {
                _snackbarMessage.value = "Failed to update name."
            }
        }
    }

    fun updateUserSurname(newSurname: String) {
        viewModelScope.launch {
            val currentUser = userRepository.getCurrentUser() ?: return@launch
            val updatedUser = currentUser.copy(surname = newSurname)
            if (userRepository.updateUserFirstName(updatedUser)) {
                fetchAllUsers()
                _snackbarMessage.value = "Surname updated successfully."
            } else {
                _snackbarMessage.value = "Failed to update surname."
            }
        }
    }

    fun sendVerificationCode(phoneNumber: String, activity: Activity) {
        if (_useTestPhoneNumber.value && phoneNumber == "+48 111111111") {
            _verificationId.value = "test-verification-id" // Use a mock verification ID
            _snackbarMessage.value = "Using test phone number. Verification code: 111111"
            return
        }

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                _snackbarMessage.value = "Verification completed automatically."
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _snackbarMessage.value = "Verification failed: ${e.localizedMessage}"
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                _verificationId.value = verificationId
                _snackbarMessage.value = "Verification code sent."
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(120L, java.util.concurrent.TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}