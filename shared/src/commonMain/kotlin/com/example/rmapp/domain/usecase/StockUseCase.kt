package com.example.rmapp.domain.usecase

import com.example.rmapp.domain.repository.StockRepository

class StockUseCase(
    private val repo: StockRepository
) {

    fun getAll() = repo.getAllStock()

    fun getLowStock() = repo.getLowStock()

    fun getTransactions(productId: String) =
        repo.getTransactions(productId)

    suspend fun produce(productId: Long, qty: Double) {
        repo.applyTransaction(productId, "PRODUCTION", qty)
    }

    suspend fun sell(productId: Long, qty: Double) {
        repo.applyTransaction(productId, "SALE", qty)
    }

    suspend fun adjust(productId: Long, qty: Double) {
        repo.applyTransaction(productId, "ADJUSTMENT", qty)
    }
}