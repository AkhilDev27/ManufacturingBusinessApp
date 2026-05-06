package com.example.rmapp.domain.usecase

import com.example.rmapp.domain.model.Product
import com.example.rmapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductUseCase(
    private val repo: ProductRepository
) {

    fun getAll(): Flow<List<Product>> = repo.getAllProducts()

    fun search(query: String): Flow<List<Product>> =
        if (query.isBlank()) repo.getAllProducts()
        else repo.searchProducts(query)

    suspend fun add(product: Product) = repo.insert(product)

    suspend fun update(product: Product) = repo.update(product)

    suspend fun delete(id: String) = repo.delete(id)
}