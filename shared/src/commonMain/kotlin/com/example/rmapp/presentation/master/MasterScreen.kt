package com.example.rmapp.presentation.master

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rmapp.presentation.TopAppBarSection

@Composable
fun MasterScreen(
    onProductsClick: () -> Unit = {},
    onCustomersClick: () -> Unit = {},
    onInventoryClick: () -> Unit = {},
    onUnitMasterClick: () -> Unit = {}
) {

    Column {
        TopAppBarSection(
            title = "Master",
            showBack = false,
            showItem = false
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {

            MasterCard(
                title = "Products",
                description = "Manage product list",
                onClick = onProductsClick
            )

            MasterCard(
                title = "Customers",
                description = "Manage customers",
                onClick = onCustomersClick
            )

            MasterCard(
                title = "Inventory",
                description = "Manage raw materials",
                onClick = onInventoryClick
            )
        }
    }
}

@Composable
fun MasterCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}