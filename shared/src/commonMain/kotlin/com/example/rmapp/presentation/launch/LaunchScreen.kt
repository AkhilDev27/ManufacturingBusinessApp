package com.example.rmapp.presentation.launch

import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun LaunchScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {

    val viewModel: LaunchViewModel = koinInject()

    LaunchedEffect(Unit) {

        delay(2000) // 2 sec splash

        val isLoggedIn = viewModel.isUserLoggedIn()

        if (isLoggedIn) {
            onNavigateToDashboard()
        } else {
            onNavigateToLogin()
        }
    }

    // Add logo / branding UI here
}