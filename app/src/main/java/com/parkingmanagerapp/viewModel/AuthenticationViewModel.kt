package com.parkingmanagerapp.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUp(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Sign Up Failed")
                }
            }
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Sign In Failed")
                }
            }
    }

    fun singOut() {
        TODO()
    }

    fun deleteAccount() {
        TODO()
    }
}