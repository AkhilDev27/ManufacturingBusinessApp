package com.example.rmapp.presentation.main

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.*
import androidx.compose.ui.Modifier
import com.example.rmapp.domain.repository.InventoryRepositoryImpl
import com.example.rmapp.domain.usecase.InventoryUseCase
import com.example.rmapp.presentation.dashboard.DashboardScreen
import com.example.rmapp.presentation.bottom_navigation.BottomNavigationBar
import com.example.rmapp.presentation.bottom_navigation.NavigationItem
import com.example.rmapp.presentation.inventory.InventoryScreen
import com.example.rmapp.presentation.inventory.InventoryViewModel
import com.example.rmapp.presentation.master.MasterScreen
import com.example.rmapp.ui.utils.AppRoutes

@Composable
fun MainContainerScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = NavigationItem.Home.route
        ) {

            composable(NavigationItem.Home.route) {
                DashboardScreen()
            }

            composable(NavigationItem.Master.route) {
                MasterScreen(
                    onProductsClick = { /* navigate to products */ },
                    onCustomersClick = { /* navigate to customers */ },
                    onInventoryClick = { navController.navigate(AppRoutes.INVENTORY) }
                )
            }

            composable(AppRoutes.INVENTORY) {
                val vm = remember {
                    InventoryViewModel(
                        InventoryUseCase(
                            InventoryRepositoryImpl()
                        )
                    )
                }
                InventoryScreen(vm)
            }

            composable(NavigationItem.Reports.route) {
                Text("Reports Screen")
            }

            composable(NavigationItem.Production.route) {
                Text("Production Screen")
            }

            composable(NavigationItem.Sales.route) {
                Text("Sales Screen")
            }
        }
    }
}