package com.example.rmapp.navigation

sealed class Screen(val route: String) {
    object Launch : Screen("launch")
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
}