package com.example.frontend_rodri.network

import com.example.frontend_rodri.models.Device
import com.example.frontend_rodri.models.LoginRequest
import com.example.frontend_rodri.models.LoginResponse
import com.example.frontend_rodri.models.RegisterRequest
import com.example.frontend_rodri.models.SaleRequest
import com.example.frontend_rodri.models.SaleResponse
import retrofit2.Response


import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Unit

    @POST("authenticate")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("dispositivos")
    suspend fun getDevices(@HeaderMap headers: Map<String, String>): List<Device>

    @POST("ventas/registrar-venta")
    suspend fun registerSale(
        @Header("Authorization") token: String,
        @Body saleRequest: SaleRequest
    ): Response<SaleResponse>
}
