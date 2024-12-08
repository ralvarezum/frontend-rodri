package com.example.frontend_rodri.models

data class RegisterRequest(
    val login: String,
    val email: String,
    val password: String
)

data class UsuarioResponse(
    val login: String, // Este debe coincidir con el backend
    val email: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String
)
