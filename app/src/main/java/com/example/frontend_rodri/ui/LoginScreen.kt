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
import com.example.frontend_rodri.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: (String) -> Unit // Recibe una función para redirigir al usuario
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
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
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.loginUser(username, password,
                        onSuccess = { response ->
                            onLoginSuccess(username) // Redirige al HomeScreen con el nombre de usuario
                        },
                        onError = { error ->
                            Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }
    }
}

