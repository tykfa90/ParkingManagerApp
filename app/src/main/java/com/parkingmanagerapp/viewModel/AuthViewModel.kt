package com.parkingmanagerapp.viewModel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.parkingmanagerapp.model.User
import com.parkingmanagerapp.model.UserRole
import com.parkingmanagerapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

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

    init {
        // Checks if a user is logged in when ViewModel is created
        checkAuthenticationState()
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
    fun sortUsersByName() {
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

    // Sends verification code
    fun sendVerificationCode(phoneNumber: String, activity: Activity) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                viewModelScope.launch {
                    _user.value?.let {
                        it.phoneNumber = credential.smsCode ?: it.phoneNumber
                        // No need to call updatePhoneNumber as it's done automatically
                    }
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _snackbarMessage.value = "Verification failed: ${e.localizedMessage}"
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                _verificationId.value = verificationId
                _snackbarMessage.value = "Verification code sent to $phoneNumber"
            }
        }

        viewModelScope.launch {
            userRepository.sendVerificationCode(phoneNumber, activity, callbacks)
        }
    }

    fun verifyAndUpdatePhoneNumber(code: String) {
        val verificationId = _verificationId.value ?: return
        viewModelScope.launch {
            if (userRepository.updatePhoneNumber(verificationId, code)) {
                _snackbarMessage.value = "Phone number updated successfully."
                _user.value?.let {
                    it.phoneNumber = code
                }
            } else {
                _snackbarMessage.value = "Failed to update phone number."
            }
        }
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

    // Allows singing in with test phone number
    fun signInWithTestPhoneNumber(phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            userRepository.signInWithTestPhoneNumber(phoneNumber, activity)
        }
    }

    // Updates user attribute
    fun updateUserAttribute(attribute: String, value: String) {
        viewModelScope.launch {
            val currentUser = _user.value ?: return@launch
            val updatedUser = when (attribute) {
                "Name" -> currentUser.copy(name = value)
                "Surname" -> currentUser.copy(surname = value)
                "Email" -> currentUser.copy(email = value)
                "Phone Number" -> currentUser.copy(phoneNumber = value)
                else -> currentUser
            }

            if (userRepository.updateUserDetails(updatedUser)) {
                _user.value = updatedUser
                _snackbarMessage.value = "$attribute updated successfully."
            } else {
                _snackbarMessage.value = "Failed to update $attribute."
            }
        }
    }
}