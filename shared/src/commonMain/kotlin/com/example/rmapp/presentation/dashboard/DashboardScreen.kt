package com.example.rmapp.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rmapp.presentation.TopAppBarSection
import org.koin.compose.koinInject

@Composable
fun DashboardScreen() {

    val viewModel: DashboardViewModel = koinInject()
    val state by viewModel.uiState.collectAsState()

    Column {
        TopAppBarSection(
            title = "Dashboard", showBack = false, showItem = false
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                DashboardCard(
                    title = "Production Today", value = state.totalProductionToday
                )
            }

            item {
                DashboardCard(
                    title = "Sales Today", value = state.totalSalesToday
                )
            }

            item {
                DashboardCard(
                    title = "Stock Value", value = state.currentStockValue
                )
            }

            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Recent Batches", style = MaterialTheme.typography.titleMedium
                )
            }

            items(state.recentBatches) { batch ->
                DashboardCard(
                    title = "Batch #${batch.id}", value = batch.outputQty.toDouble()
                )
            }

            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Low Stock Alerts", style = MaterialTheme.typography.titleMedium
                )
            }

            items(state.lowStockItems) { stock ->
                DashboardCard(
                    title = stock.productName, value = stock.quantity, isAlert = true
                )
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String, value: Double, isAlert: Boolean = false
) {

    Card {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(title, style = MaterialTheme.typography.titleMedium)

            Text(
                text = value.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = if (isAlert) Color.Red else Color.Unspecified
            )
        }
    }
}