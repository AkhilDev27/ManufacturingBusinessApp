package com.example.rmapp.presentation.dashboard.model

import com.example.rmapp.domain.model.ProductionBatch
import com.example.rmapp.domain.model.StockItem

data class DashboardData(
    val totalProductionToday: Double = 0.0,
    val totalSalesToday: Double = 0.0,
    val currentStockValue: Double = 0.0,
    val recentBatches: List<ProductionBatch> = emptyList(),
    val lowStockItems: List<StockItem> = emptyList()
)