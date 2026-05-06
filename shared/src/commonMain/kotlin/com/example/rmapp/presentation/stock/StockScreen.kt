package com.example.rmapp.presentation.stock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rmapp.domain.model.Stock
import com.example.rmapp.presentation.TopAppBarSection

@Composable
fun StockScreen(vm: StockViewModel) {

    val items by vm.items.collectAsState()
    val lowStock by vm.lowStock.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarSection(
                title = "Stock",
                showBack = true
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            // ⚠ LOW STOCK SECTION
            if (lowStock.isNotEmpty()) {

                Text(
                    text = "⚠ Low Stock",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(Modifier.height(8.dp))

                lowStock.forEach { item ->
                    Text(
                        text = "Product ${item.productId} → ${item.quantity}"
                    )
                }

                Spacer(Modifier.height(16.dp))
            }

            // 📦 STOCK LIST
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(items) { item ->
                    StockItemCard(item, vm)
                }
            }
        }
    }
}

@Composable
fun StockItemCard(
    item: Stock,
    vm: StockViewModel
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            // 📦 DETAILS
            Text(
                text = "Product ID: ${item.productId}",
                style = MaterialTheme.typography.titleMedium
            )

            Text("Quantity: ${item.quantity}")

            // ⚠ Low Stock Warning
            if (item.quantity <= item.minimumThreshold) {
                Text(
                    text = "⚠ Low Stock",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(8.dp))

            // 🔘 ACTIONS
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = { vm.produce(item.productId) }
                ) {
                    Text("+ Produce")
                }

                Button(
                    onClick = { vm.sell(item.productId) }
                ) {
                    Text("- Sell")
                }
            }
        }
    }
}