package com.example.frontend_rodri.network

import com.example.frontend_rodri.models.LoginRequest
import com.example.frontend_rodri.models.LoginResponse
import com.example.frontend_rodri.models.RegisterRequest

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Unit

    @POST("authenticate")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
