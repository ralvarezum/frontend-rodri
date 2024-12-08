package com.example.frontend_rodri.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_rodri.models.LoginResponse
import com.example.frontend_rodri.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {

    fun loginUser(username: String, password: String, onSuccess: (LoginResponse) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = userRepository.login(username, password)
                onSuccess(response)
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }
        }
    }
}
