package com.example.rmapp.presentation.bottom_navigation

import org.jetbrains.compose.resources.DrawableResource
import rmapp.shared.generated.resources.Res
import rmapp.shared.generated.resources.ic_home
import rmapp.shared.generated.resources.ic_manufacture
import rmapp.shared.generated.resources.ic_master
import rmapp.shared.generated.resources.ic_report
import rmapp.shared.generated.resources.ic_sales

sealed class NavigationItem(
    val route: String,
    val title: String,
    val icon: DrawableResource
) {
    object Home : NavigationItem("home", "Home", Res.drawable.ic_home)
    object Master : NavigationItem("master", "Master", Res.drawable.ic_master)
    object Reports : NavigationItem("reports", "Reports", Res.drawable.ic_report)
    object Production : NavigationItem("production", "Production", Res.drawable.ic_manufacture)
    object Sales : NavigationItem("sales", "Sales", Res.drawable.ic_sales)
}