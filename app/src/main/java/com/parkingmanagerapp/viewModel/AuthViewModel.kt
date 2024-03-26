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
    private val _signInStatus = MutableStateFlow<Boolean?>(null)
    val signInStatus = _signInStatus.asStateFlow()
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    // Sign-in fails// Sign-in success
                    _signInStatus.value = task.isSuccessful
                    _snackbarMessage.value = "Sign in successful!"
                }
            } catch (e: Exception) {
                _signInStatus.value = false
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Assuming the user is automatically signed in after registration
                        _signInStatus.value = true // Indicating a successful sign-in
                        _snackbarMessage.value = "Registration successful!"
                    } else {
                        _signInStatus.value = false // Indicate failure
                    }
                }
            } catch (e: Exception) {
                _signInStatus.value = false // Handle exception by indicating failure
            }
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null // Reset the message to prevent repeated displays
    }

    fun setSnackbarMessage(message: String) {
        _snackbarMessage.value = message // Display set message inside a snackbar
    }
}
