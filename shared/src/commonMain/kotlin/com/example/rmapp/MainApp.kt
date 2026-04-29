package com.example.rmapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.rmapp.navigation.Screen
import com.example.rmapp.presentation.dashboard.DashboardScreen
import com.example.rmapp.presentation.launch.LaunchScreen
import com.example.rmapp.presentation.login.LoginScreen
import com.example.rmapp.presentation.main.MainContainerScreen
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration
import org.koin.core.module.Module

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MainApp(appModule: Module) {

    KoinMultiplatformApplication(
        config = koinConfiguration { modules(appModule) }
    ) {

        MaterialTheme {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Screen.Launch.route
            ) {

                // -------------------------------
                // LAUNCH (SPLASH + LOGIC)
                // -------------------------------
                composable(Screen.Launch.route) {
                    LaunchScreen(
                        onNavigateToLogin = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Launch.route) { inclusive = true }
                            }
                        },
                        onNavigateToDashboard = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Launch.route) { inclusive = true }
                            }
                        }
                    )
                }

                // -------------------------------
                // LOGIN
                // -------------------------------
                composable(Screen.Login.route) {
                    LoginScreen(
                        onSuccess = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    )
                }

                // -------------------------------
                // DASHBOARD
                // -------------------------------
                composable(Screen.Dashboard.route) {
                    MainContainerScreen()
                }
            }
        }
    }
}