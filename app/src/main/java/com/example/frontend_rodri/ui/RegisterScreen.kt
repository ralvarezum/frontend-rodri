package com.example.frontend_rodri.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend_rodri.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel(),
    onNavigateToLogin: () -> Unit // Función de navegación
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                println("Botón de registrar presionado")
                coroutineScope.launch {
                    try {
                        viewModel.registerUser(username, email, password,
                            onSuccess = {
                                println("Usuario registrado con éxito")
                            },
                            onError = { error ->
                                println("Error al registrar usuario: $error")
                            }
                        )
                    } catch (e: Exception) {
                        println("Excepción al intentar registrar: ${e.message}")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para ir a la pantalla de Login
        Button(
            onClick = { onNavigateToLogin() }, // Navega a LoginScreen
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿Ya tienes una cuenta? Inicia sesión")
        }
    }
}

