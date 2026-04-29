package com.example.rmapp.presentation.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rmapp.presentation.TopAppBarSection
import org.koin.compose.koinInject

@Composable
fun DashboardScreen() {

    val viewModel: DashboardViewModel = koinInject()
    val state by viewModel.uiState.collectAsState()

    Column {
        TopAppBarSection(
            title = "Dashboard",
            showBack = false,
            showItem = false
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            item {
                DashboardCard("Production Today", state.totalProductionToday)
                DashboardCard("Sales Today", state.totalSalesToday)
                DashboardCard("Stock Value", state.currentStockValue)
            }

            item { Spacer(Modifier.height(16.dp)) }

            item {
                Text("Recent Batches", style = MaterialTheme.typography.titleMedium)
            }

            items(state.recentBatches) { batch ->
                DashboardCard(
                    title = "Batch #${batch.id}",
                    value = batch.outputQty
                )
            }

            item { Spacer(Modifier.height(16.dp)) }

            item {
                Text("Low Stock Alerts", style = MaterialTheme.typography.titleMedium)
            }

            items(state.lowStockItems) { stock ->
                DashboardCard(
                    title = stock.name,
                    value = stock.quantity,
                    isAlert = true
                )
            }
        }
    }
}