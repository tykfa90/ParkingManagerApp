package com.parkingmanagerapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth
    private val _signInStatus = MutableStateFlow<Boolean?>(null)
    val signInStatus = _signInStatus.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    // Sign-in fails// Sign-in success
                    _signInStatus.value = task.isSuccessful
                }
            } catch (e: Exception) {
                _signInStatus.value = false
            }
        }
    }
}
