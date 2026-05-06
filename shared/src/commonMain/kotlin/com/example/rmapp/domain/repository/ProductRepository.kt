package com.example.rmapp.domain.repository

import com.example.rmapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getAllProducts(): Flow<List<Product>>

    fun searchProducts(query: String): Flow<List<Product>>

    suspend fun insert(product: Product)

    suspend fun update(product: Product)

    suspend fun delete(id: String)
}