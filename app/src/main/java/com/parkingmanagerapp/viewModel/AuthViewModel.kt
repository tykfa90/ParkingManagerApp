package com.parkingmanagerapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parkingmanagerapp.repository.UserRepository
import com.parkingmanagerapp.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _signInStatus = MutableStateFlow<Boolean?>(null)
    val signInStatus = _signInStatus.asStateFlow()

    private val _signUpStatus = MutableStateFlow<Boolean?>(null)
    val signUpStatus = _signUpStatus.asStateFlow()

    fun signIn(email: String, password: String) = viewModelScope.launch {
        _signInStatus.value = userRepository.signInUser(email, password) != null
    }

    fun signUp(user: User, password: String) = viewModelScope.launch {
        _signUpStatus.value = userRepository.signUpUser(user, password)
    }
}
