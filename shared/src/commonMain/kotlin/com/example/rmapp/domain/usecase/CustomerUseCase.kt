package com.example.rmapp.domain.usecase

import com.example.rmapp.domain.model.Customer
import com.example.rmapp.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow

class CustomerUseCase(
    private val repo: CustomerRepository
) {

    fun getAll(): Flow<List<Customer>> = repo.getAllCustomers()

    fun search(query: String): Flow<List<Customer>> =
        if (query.isBlank()) repo.getAllCustomers()
        else repo.searchCustomers(query)

    suspend fun add(customer: Customer) = repo.insert(customer)

    suspend fun update(customer: Customer) = repo.update(customer)

    suspend fun delete(id: String) = repo.delete(id)
}