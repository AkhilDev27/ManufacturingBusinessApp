package com.example.rmapp.presentation.main

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rmapp.presentation.bottom_navigation.BottomNavigationBar
import com.example.rmapp.presentation.bottom_navigation.NavigationItem
import com.example.rmapp.presentation.customers.CustomerScreen
import com.example.rmapp.presentation.customers.CustomerViewModel
import com.example.rmapp.presentation.dashboard.DashboardScreen
import com.example.rmapp.presentation.inventory.InventoryFormScreen
import com.example.rmapp.presentation.inventory.InventoryScreen
import com.example.rmapp.presentation.inventory.InventoryViewModel
import com.example.rmapp.presentation.master.MasterScreen
import com.example.rmapp.presentation.products.ProductScreen
import com.example.rmapp.presentation.products.ProductViewModel
import com.example.rmapp.presentation.stock.StockScreen
import com.example.rmapp.presentation.stock.StockViewModel
import com.example.rmapp.ui.utils.AppRoutes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainContainerScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }) { padding ->

        NavHost(
            navController = navController, startDestination = NavigationItem.Home.route
        ) {

            composable(NavigationItem.Home.route) {
                DashboardScreen()
            }

            composable(NavigationItem.Master.route) {
                MasterScreen(
                    onProductsClick = { navController.navigate(AppRoutes.PRODUCTS) },
                    onCustomersClick = { navController.navigate(AppRoutes.CUSTOMERS) },
                    onInventoryClick = { navController.navigate(AppRoutes.INVENTORY) },
                    onUnitMasterClick = { navController.navigate(AppRoutes.UNIT_MASTER) })
            }

            composable(AppRoutes.STOCK) {
                val vm: StockViewModel = koinViewModel()
                StockScreen(vm)
            }

            composable(AppRoutes.PRODUCTS) {
                val vm: ProductViewModel = koinViewModel()
                ProductScreen(vm, navController)
            }

            composable(AppRoutes.CUSTOMERS) {
                val vm: CustomerViewModel = koinViewModel()
                CustomerScreen(vm, navController)
            }

            composable(AppRoutes.INVENTORY) {
                val vm: InventoryViewModel = koinViewModel()
                InventoryScreen(vm, navController)
            }

            composable(AppRoutes.INVENTORY_FORM) {
                val vm: InventoryViewModel = koinViewModel()
                InventoryFormScreen(vm, navController, null)
            }

            composable(AppRoutes.INVENTORY_FORM_WITH_ID) { backStack ->
                val vm: InventoryViewModel = koinViewModel()
                val id = backStack.arguments?.getString("inventoryId")
                InventoryFormScreen(vm, navController, id)
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