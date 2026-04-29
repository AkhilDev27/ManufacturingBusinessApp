package com.example.rmapp.data.mapper

import com.example.rmapp.db.stock.Stock
import com.example.rmapp.domain.model.StockItem

fun Stock.toDomain(): StockItem {
    return StockItem(
        id = id,
        name = name,
        quantity = quantity,
        unitPrice = unitPrice,
        minimumThreshold = minimumThreshold
    )
}