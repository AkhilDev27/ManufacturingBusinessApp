package com.example.rmapp.data.mapper

import com.example.rmapp.db.stock.Stock

fun Stock.toDomain(): Stock {
    return Stock(
        id = id,
        productId = productId,
        quantity = quantity,
        minimumThreshold = minimumThreshold
    )
}