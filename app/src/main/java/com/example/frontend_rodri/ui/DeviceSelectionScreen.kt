package com.example.frontend_rodri.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend_rodri.models.AdditionalOption
import com.example.frontend_rodri.models.CustomizationOption
import com.example.frontend_rodri.models.Device
import com.example.frontend_rodri.models.SaleRequest
import com.example.frontend_rodri.viewmodel.DeviceViewModel
import com.example.frontend_rodri.viewmodel.SharedViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceSelectionScreen(
    sharedViewModel: SharedViewModel,
    viewModel: DeviceViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val token by sharedViewModel.token.collectAsState()
    val devices by viewModel.devices.collectAsState()
    var selectedDevice by remember { mutableStateOf<Device?>(null) }
    var selectedOptions by remember { mutableStateOf<Map<String, Long>>(emptyMap()) }
    var selectedAccessories by remember { mutableStateOf<List<Long>>(emptyList()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val finalPrice by remember(selectedDevice, selectedOptions, selectedAccessories) {
        derivedStateOf {
            selectedDevice?.let {
                calculateFinalPrice(selectedOptions, selectedAccessories, it)
            } ?: 0.0
        }
    }

    LaunchedEffect(token) {
        token?.let { viewModel.loadDevices(it) }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Seleccionar Dispositivo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("Selecciona un dispositivo", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            var expandedDevice by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = { expandedDevice = true }) {
                    Text(selectedDevice?.nombre ?: "Selecciona un dispositivo")
                }
                DropdownMenu(expanded = expandedDevice, onDismissRequest = { expandedDevice = false }) {
                    devices.forEach { device ->
                        DropdownMenuItem(
                            onClick = {
                                selectedDevice = device
                                expandedDevice = false
                                selectedOptions = emptyMap()
                                selectedAccessories = emptyList()
                            },
                            text = { Text(device.nombre) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            selectedDevice?.let { device ->
                Text("Selecciona personalizaciones")
                Spacer(modifier = Modifier.height(8.dp))
                device.personalizaciones.forEach { customization ->
                    var expandedOption by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        TextButton(onClick = { expandedOption = true }) {
                            val selectedOptionName = customization.opciones.find {
                                it.id == selectedOptions[customization.nombre]
                            }?.nombre ?: "Selecciona una opción para ${customization.nombre}"
                            Text(selectedOptionName)
                        }
                        DropdownMenu(expanded = expandedOption, onDismissRequest = { expandedOption = false }) {
                            customization.opciones.forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedOptions = selectedOptions + (customization.nombre to option.id)
                                        expandedOption = false
                                    },
                                    text = { Text(option.nombre) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Selecciona adicionales")
                Spacer(modifier = Modifier.height(8.dp))
                device.adicionales.forEach { accessory ->
                    var isChecked by remember {
                        mutableStateOf(selectedAccessories.contains(accessory.id))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                isChecked = it
                                selectedAccessories = if (it) {
                                    selectedAccessories + accessory.id
                                } else {
                                    selectedAccessories - accessory.id
                                }
                            }
                        )
                        Text(accessory.nombre)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Precio total: \$${"%.2f".format(finalPrice)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val saleRequest = SaleRequest(
                                    idDispositivo = selectedDevice!!.id,
                                    personalizaciones = selectedOptions.map { (key, value) ->
                                        val price = device.personalizaciones
                                            .first { it.nombre == key }
                                            .opciones.first { it.id == value }.precioAdicional
                                        CustomizationOption(value, price)
                                    },
                                    adicionales = selectedAccessories.map { accessoryId ->
                                        val price = device.adicionales
                                            .first { it.id == accessoryId }.precio
                                        AdditionalOption(accessoryId, price)
                                    },
                                    precioFinal = finalPrice,
                                    fechaVenta = getCurrentDateTime()
                                )

                                val result = viewModel.registerSale(token!!, saleRequest)
                                result.onSuccess { saleResponse ->
                                    snackbarHostState.showSnackbar("Venta registrada con éxito: ${saleResponse.id}")
                                }.onFailure { exception ->
                                    snackbarHostState.showSnackbar("Error al registrar la venta: ${exception.message}")
                                }
                            }
                        },
                        enabled = selectedDevice != null
                    ) {
                        Text("Comprar")
                    }
                }
            }
        }
    }
}

fun calculateFinalPrice(
    selectedOptions: Map<String, Long>,
    selectedAccessories: List<Long>,
    device: Device
): Double {
    val basePrice = device.precioBase.toDouble()

    val customizationTotal = selectedOptions.values.sumOf { optionId ->
        device.personalizaciones.flatMap { it.opciones }
            .first { it.id == optionId }.precioAdicional
    }

    val currentPrice = basePrice + customizationTotal

    val additionalTotal = selectedAccessories.sumOf { accessoryId ->
        val accessory = device.adicionales.first { it.id == accessoryId }
        if (accessory.precioGratis != -1 && currentPrice >= accessory.precioGratis) {
            0.0
        } else {
            accessory.precio
        }
    }

    // Precio total final
    return basePrice + customizationTotal + additionalTotal
}

fun getCurrentDateTime(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    return current.format(formatter)
}
