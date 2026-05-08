package com.example.rmapp.data.mapper

import com.example.rmapp.domain.model.Product

fun Product.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        sellingPrice = sellingPrice,
        costPrice = costPrice
    )
}