package com.example.rmapp.domain.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.rmapp.db.customers.CustomerQueries
import com.example.rmapp.domain.model.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CustomerRepositoryImpl(
    private val queries: CustomerQueries
) : CustomerRepository {

    override fun getAllCustomers(): Flow<List<Customer>> =
        queries.getAllCustomers()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map {
                    Customer(
                        id = it.id.toString(),
                        name = it.name,
                        phone = it.phone ?: "",
                        address = it.address ?: ""
                    )
                }
            }

    override fun searchCustomers(query: String): Flow<List<Customer>> =
        queries.searchCustomers(query)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map {
                    Customer(
                        id = it.id.toString(),
                        name = it.name,
                        phone = it.phone ?: "",
                        address = it.address ?: ""
                    )
                }
            }

    override suspend fun insert(customer: Customer) {
        queries.insertCustomer(
            name = customer.name,
            phone = customer.phone,
            address = customer.address
        )
    }

    override suspend fun update(customer: Customer) {
        queries.updateCustomer(
            name = customer.name,
            phone = customer.phone,
            address = customer.address,
            id = customer.id.toLong()
        )
    }

    override suspend fun delete(id: String) {
        queries.deleteCustomer(id.toLong())
    }
}