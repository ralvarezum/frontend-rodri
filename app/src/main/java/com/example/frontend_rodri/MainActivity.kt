package com.example.frontend_rodri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.navArgument
import com.example.frontend_rodri.ui.DeviceSelectionScreen
import com.example.frontend_rodri.ui.HomeScreen
import com.example.frontend_rodri.ui.LoginScreen
import com.example.frontend_rodri.ui.RegisterScreen
import com.example.frontend_rodri.viewmodel.SharedViewModel
import com.example.frontend_rodri.ui.theme.FrontendrodriTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontendrodriTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val sharedViewModel = viewModel<SharedViewModel>()

    NavHost(navController = navController, startDestination = "register") {
        composable("register") {
            RegisterScreen(onNavigateToLogin = { navController.navigate("login") })
        }
        composable("login") {
            LoginScreen(
                onLoginSuccess = { token, username ->
                    sharedViewModel.setToken(token)
                    navController.navigate("home/$username")
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable(
            route = "home/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Usuario"
            HomeScreen(
                username = username,
                onNavigateToDeviceSelection = { navController.navigate("deviceSelection") }
            )
        }
        composable("deviceSelection") {
            DeviceSelectionScreen(
                sharedViewModel = sharedViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
