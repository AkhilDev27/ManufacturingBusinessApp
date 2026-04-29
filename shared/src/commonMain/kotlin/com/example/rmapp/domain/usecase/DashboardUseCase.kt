package com.example.rmapp.domain.usecase

import com.example.rmapp.domain.repository.ProductionRepository
import com.example.rmapp.domain.repository.SalesRepository
import com.example.rmapp.domain.repository.StockRepository
import com.example.rmapp.presentation.dashboard.model.DashboardData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class DashboardUseCase(
    private val productionRepo: ProductionRepository,
    private val salesRepo: SalesRepository,
    private val stockRepo: StockRepository
) {

    operator fun invoke(): Flow<DashboardData> {

        return combine(
            productionRepo.getTodayProduction(),   // Flow<List<ProductionBatch>>
            salesRepo.getTodaySales(),             // Flow<List<Sale>>
            stockRepo.getAllStock(),               // Flow<List<StockItem>>
            productionRepo.getRecentBatches()      // Flow<List<ProductionBatch>>
        ) { productionList, salesList, stockList, recentBatches ->

            val totalProduction = productionList.sumOf { it.outputQty }

            val totalSales = salesList.sumOf { it.totalAmount }

            val stockValue = stockList.sumOf {
                it.quantity * it.unitPrice
            }

            val lowStock = stockList.filter {
                it.quantity < it.minimumThreshold
            }

            DashboardData(
                totalProductionToday = totalProduction,
                totalSalesToday = totalSales,
                currentStockValue = stockValue,
                recentBatches = recentBatches.take(5),
                lowStockItems = lowStock
            )
        }
    }
}