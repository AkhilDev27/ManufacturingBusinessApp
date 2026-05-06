package com.example.rmapp.domain.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.rmapp.db.products.ProductsQueries
import com.example.rmapp.domain.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val queries: ProductsQueries
) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> =
        queries.getAllProducts()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map {
                    Product(
                        id = it.id.toString(),
                        name = it.name,
                        unit = it.unit,
                        sellingPrice = it.sellingPrice ?: 0.0,
                        costPrice = it.costPrice ?: 0.0
                    )
                }
            }

    override fun searchProducts(query: String): Flow<List<Product>> =
        queries.searchProducts(query)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map {
                    Product(
                        id = it.id.toString(),
                        name = it.name,
                        unit = it.unit,
                        sellingPrice = it.sellingPrice ?: 0.0,
                        costPrice = it.costPrice ?: 0.0
                    )
                }
            }

    override suspend fun insert(product: Product) {
        queries.insertProduct(
            name = product.name,
            unit = product.unit,
            sellingPrice = product.sellingPrice,
            costPrice = product.costPrice
        )
    }

    override suspend fun update(product: Product) {
        queries.updateProduct(
            name = product.name,
            unit = product.unit,
            sellingPrice = product.sellingPrice,
            costPrice = product.costPrice,
            id = product.id.toLong()
        )
    }

    override suspend fun delete(id: String) {
        queries.deleteProduct(id.toLong())
    }
}