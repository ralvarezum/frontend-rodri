package com.example.frontend_rodri.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_rodri.models.Device
import com.example.frontend_rodri.models.SaleRequest
import com.example.frontend_rodri.models.SaleResponse
import com.example.frontend_rodri.network.ApiClient.apiService
import com.example.frontend_rodri.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeviceViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {
    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices

    fun loadDevices(token: String) {
        viewModelScope.launch {
            try {
                val devicesFromBackend = repository.fetchDevices(token)
                _devices.value = devicesFromBackend
                Log.d("DeviceViewModel", "Dispositivos cargados: ${devicesFromBackend.size}")
            } catch (e: Exception) {
                Log.e("DeviceViewModel", "Error al cargar dispositivos: ${e.message}")
            }
        }
    }

    suspend fun registerSale(token: String, saleRequest: SaleRequest): Result<SaleResponse> {
        return try {
            val response = apiService.registerSale("Bearer $token", saleRequest)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Cuerpo de respuesta vacío"))
            } else {
                Result.failure(Exception("Error en la API: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
