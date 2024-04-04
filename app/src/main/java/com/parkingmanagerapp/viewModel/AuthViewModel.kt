package com.parkingmanagerapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth

    // Observing sign-in status
    private val _signInStatus = MutableStateFlow<Boolean?>(null)
    val signInStatus = _signInStatus.asStateFlow()

    // Observing if a user is authenticated
    private val _isUserAuthenticated = MutableStateFlow<Boolean?>(null)
    val isUserAuthenticated = _isUserAuthenticated.asStateFlow()

    // Managing Snackbar messages
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    init {
        // Check if a user is logged in when ViewModel is created
        _isUserAuthenticated.value = auth.currentUser != null
    }

    // Function to handle sign-in process
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        _signInStatus.value = false
                        _snackbarMessage.value = "Sign in failed: ${task.exception?.message}"
                    } else {
                        _signInStatus.value = true // Sign-in success
                        _snackbarMessage.value = "Sign in successful!"
                        // Update isUserAuthenticated on successful sign in
                        _isUserAuthenticated.value = true
                    }
                }
            } catch (e: Exception) {
                _signInStatus.value = false
                _snackbarMessage.value = "Sign in failed: ${e.message}"
            }
        }
    }

    // Function to handle registering new user
    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _signInStatus.value = true // Sign-in success
                        _snackbarMessage.value = "Registration successful!"
                        // Update isUserAuthenticated on successful registration
                        _isUserAuthenticated.value = true
                    } else {
                        _signInStatus.value = false // Registration failure
                        _snackbarMessage.value = "Registration failed: ${task.exception?.message}"
                    }
                }
            } catch (e: Exception) {
                _signInStatus.value = false // Handle exception by indicating failure
                _snackbarMessage.value = "Registration failed: ${e.message}"
            }
        }
    }

    // Function to sign-out current user
    fun signOut() {
        auth.signOut()
        _isUserAuthenticated.value = false // Update isUserAuthenticated on sign out
        _signInStatus.value = null // Reset signInStatus as we're now signed out
    }

    // Remove snackbar off the screen
    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

    // Setter for snackbar message
    fun setSnackbarMessage(message: String) {
        _snackbarMessage.value = message
    }
}