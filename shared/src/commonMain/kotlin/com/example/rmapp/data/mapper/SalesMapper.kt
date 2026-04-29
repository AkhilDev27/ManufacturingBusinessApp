package com.example.rmapp.data.mapper

import com.example.rmapp.db.sales.Sales
import com.example.rmapp.domain.model.Sale

fun Sales.toDomain(): Sale {
    return Sale(
        id = id,
        customerId = customerId,
        totalAmount = totalAmount,
        date = date
    )
}