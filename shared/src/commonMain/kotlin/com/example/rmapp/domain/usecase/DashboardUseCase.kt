package com.example.rmapp.domain.usecase

import com.example.rmapp.domain.model.StockUI
import com.example.rmapp.domain.repository.ProductRepository
import com.example.rmapp.domain.repository.ProductionRepository
import com.example.rmapp.domain.repository.SalesRepository
import com.example.rmapp.domain.repository.StockRepository
import com.example.rmapp.presentation.dashboard.model.DashboardData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class DashboardUseCase(
    private val productionRepo: ProductionRepository,
    private val salesRepo: SalesRepository,
    private val stockRepo: StockRepository,
    private val productRepo: ProductRepository
) {

    operator fun invoke(): Flow<DashboardData> {

        return combine(
            productionRepo.getTodayProduction(),
            salesRepo.getTodaySales(),
            stockRepo.getAllStock(),
            productionRepo.getRecentBatches(),
            productRepo.getAllProducts()
        ) { productionList,
            salesList,
            stockList,
            recentBatches,
            productList ->

            val totalProduction = productionList.sumOf { it.outputQty.toDouble() }

            val totalSales = salesList.sumOf { it.totalAmount.toDouble() }

            val stockValue = stockList.sumOf { stock ->
                val product = productList.firstOrNull { it.id == stock.productId }
                val price = product?.sellingPrice ?: 0.0
                stock.quantity * price
            }

            val lowStock = stockList.mapNotNull { stock ->
                val product = productList.firstOrNull { it.id == stock.productId }
                product?.let {
                    StockUI(
                        productId = stock.productId,
                        productName = it.name,
                        quantity = stock.quantity,
                        minimumThreshold = stock.minimumThreshold
                    )
                }
            }.filter {
                it.quantity <= it.minimumThreshold
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