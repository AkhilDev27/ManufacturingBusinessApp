package com.example.rmapp.data.mapper

import com.example.rmapp.db.products.Product

fun Product.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        unit = unit,
        sellingPrice = sellingPrice,
        costPrice = costPrice
    )
}