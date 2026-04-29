package com.example.rmapp.data.mapper

import com.example.rmapp.db.production.Production
import com.example.rmapp.domain.model.ProductionBatch

fun Production.toDomain(): ProductionBatch {
    return ProductionBatch(
        id = id,
        date = date,
        inputQty = inputQty,
        outputQty = outputQty,
        wastageQty = wastageQty
    )
}