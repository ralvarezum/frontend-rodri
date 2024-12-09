package com.example.frontend_rodri.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class RegisterRequest(
    val login: String,
    val email: String,
    val password: String
)

data class UsuarioResponse(
    val login: String,
    val email: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("id_token") val token: String
)

data class Device(
    val id: Long,
    val codigo: String,
    val nombre: String,
    val descripcion: String,
    val precioBase: BigDecimal,
    val personalizaciones: List<Customization>,
    val adicionales: List<Accessory>
)

data class Customization(
    val id: Long,
    val nombre: String,
    val opciones: List<Option>
)

data class Option(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val precioAdicional: Double
)

data class Accessory(
    val id: Long,
    val nombre: String,
    val precio: Double,
    val precioGratis: Int
)

data class SaleRequest(
    val idDispositivo: Long,
    val personalizaciones: List<CustomizationOption>,
    val adicionales: List<AdditionalOption>,
    val precioFinal: Double,
    val fechaVenta: String
)

data class CustomizationOption(
    val id: Long,
    val precio: Double
)

data class AdditionalOption(
    val id: Long,
    val precio: Double
)


data class SaleResponse(
    val id: Long,
    val fechaVenta: String,
    val precioFinal: Double,
    val dispositivo: DeviceResponse,
    val adicionals: List<AdditionalResponse>,
    val personalizacions: List<CustomizationResponse>
)

data class DeviceResponse(
    val id: Long,
    val codigo: String?,
    val nombre: String?,
    val descripcion: String?,
    val precioBase: Double?,
    val moneda: String?,
    val idCatedra: Long?,
    val caracteristicas: String?,
    val personalizaciones: List<CustomizationResponse>?,
    val adicionales: List<AdditionalResponse>?
)

data class AdditionalResponse(
    val id: Long,
    val nombre: String?,
    val descripcion: String?,
    val precio: Double?,
    val precioGratis: Double?,
    val idCatedra: Long?,
    val dispositivo: DeviceResponse?,
    val ventas: List<SaleResponse>?
)

data class CustomizationResponse(
    val id: Long,
    val nombre: String?,
    val descripcion: String?,
    val idCatedra: Long?,
    val dispositivo: DeviceResponse?,
    val opciones: List<OptionResponse>?,
    val ventas: List<SaleResponse>?
)

data class OptionResponse(
    val id: Long,
    val nombre: String?,
    val precio: Double?
)

