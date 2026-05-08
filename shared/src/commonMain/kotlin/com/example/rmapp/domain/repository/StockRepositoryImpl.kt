package com.example.rmapp.domain.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.rmapp.db.stock.StockQueries
import com.example.rmapp.db.stocktransaction.StockTransactionQueries
import com.example.rmapp.domain.model.Stock
import com.example.rmapp.domain.model.StockTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StockRepositoryImpl(
    private val stockQueries: StockQueries,
    private val txQueries: StockTransactionQueries
) : StockRepository {

    override fun getAllStock(): Flow<List<Stock>> =
        stockQueries.getAllStock()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map {
                    Stock(
                        id = it.id.toString(),
                        productId = it.productId,
                        quantity = it.quantity,
                        minimumThreshold = it.minimumThreshold
                    )
                }
            }

    override fun getLowStock(): Flow<List<Stock>> =
        stockQueries.getLowStock()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map {
                    Stock(
                        id = it.id.toString(),
                        productId = it.productId,
                        quantity = it.quantity,
                        minimumThreshold = it.minimumThreshold
                    )
                }
            }

    override fun getTransactions(productId: String): Flow<List<StockTransaction>> =
        txQueries.getTransactionsByProduct(productId.toLong())
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map {
                    StockTransaction(
                        id = it.id.toString(),
                        productId = it.productId.toString(),
                        type = it.type,
                        quantity = it.quantity,
                        note = it.note,
                        createdAt = it.createdAt ?: ""
                    )
                }
            }

    override suspend fun applyTransaction(
        productId: Long,
        type: String,
        quantity: Double,
        note: String?
    ) {

        val delta = when (type) {
            "PRODUCTION" -> quantity
            "SALE" -> -quantity
            "ADJUSTMENT" -> quantity
            else -> 0.0
        }

        stockQueries.transaction {

            stockQueries.updateStockQuantity(
                delta,
                productId
            )

            txQueries.insertTransaction(
                productId = productId,
                type = type,
                quantity = quantity,
                note = note
            )
        }
    }
}