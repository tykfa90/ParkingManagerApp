package com.parkingmanagerapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    val _signInStatus = MutableStateFlow<Boolean?>(null)
    val signInStatus = _signInStatus.asStateFlow()
    val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    // Snackbar handling
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    init {
        // Check if a user is logged in when ViewModel is created
        checkAuthenticationState()
    }

    // Registers a new user in the FirebaseAuth database. After generating uid,
    // passes over the newUser account with additional data to the Firebase Firestore.
    fun registerWithFirebase(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            val newUser = User(
                name = "",
                surname = "",
                phoneNumber = "",
                email = email,
                role = UserRole.REGULAR,
                uid = "",
                reservations = emptyList()
            )

            val success = userRepository.registerNewUser(newUser, password)
            if (success) {
                // Upon successful registration, update the UI state and user flow.
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

    fun signOut() {
        viewModelScope.launch {
            userRepository.signOutUser()
            _user.value = null
            _signInStatus.value = null
            _snackbarMessage.value = "Signed out successfully."
        }
    }

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

    fun updateUserProfile(
        name: String,
        surname: String,
        phoneNumber: String,
        email: String
    ) {
        viewModelScope.launch {
            // Fetch current user's role and reservations to preserve them in the update
            val currentUser = _user.value
            if (currentUser != null) {
                val updatedUser = currentUser.copy(
                    name = name,
                    surname = surname,
                    phoneNumber = phoneNumber,
                    email = email
                )
                val success = userRepository.updateUserDetails(updatedUser)
                if (success) {
                    _user.value = updatedUser
                    _snackbarMessage.value = "Profile updated successfully"
                } else {
                    _snackbarMessage.value = "Failed to update profile"
                }
            }
        }
    }

    // Updates user role. Usage limited strictly to ADMIN level accounts.
    fun updateUserRole(targetUserId: String, newRole: UserRole) {
        viewModelScope.launch {
            // Only allow admins to update user roles
            if (_user.value?.role == UserRole.ADMIN) {
                val success = userRepository.updateUserRole(targetUserId, newRole)
                if (success) {
                    _snackbarMessage.value = "User role updated successfully"
                } else {
                    _snackbarMessage.value = "Failed to update user role"
                }
            } else {
                _snackbarMessage.value = "Unauthorized to update roles"
            }
        }
    }
}