package com.example.frontend_rodri.repository

import com.example.frontend_rodri.models.LoginRequest
import com.example.frontend_rodri.models.LoginResponse
import com.example.frontend_rodri.models.RegisterRequest
import com.example.frontend_rodri.models.UsuarioResponse
import com.example.frontend_rodri.network.ApiClient

class UserRepository {

    private val apiService = ApiClient.apiService // Usa la instancia de `ApiService`

    suspend fun register(username: String, email: String, password: String) {
        // Crea el objeto `RegisterRequest` con los datos necesarios
        val request = RegisterRequest(
            login = username, // Backend espera 'login'
            email = email,
            password = password
        )
        // Llama al método `register` de ApiService con el objeto `RegisterRequest`
        println("Datos enviados al backend: $request")
        apiService.register(request) // Ahora no devolvemos nada
    }

    suspend fun login(username: String, password: String): LoginResponse {
        val request = LoginRequest(username, password)
        return apiService.login(request)
    }
}
