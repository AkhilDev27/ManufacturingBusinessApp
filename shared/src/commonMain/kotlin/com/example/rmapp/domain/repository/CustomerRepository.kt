package com.example.rmapp.domain.repository

import com.example.rmapp.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

    fun getAllCustomers(): Flow<List<Customer>>

    fun searchCustomers(query: String): Flow<List<Customer>>

    suspend fun insert(customer: Customer)

    suspend fun update(customer: Customer)

    suspend fun delete(id: String)
}