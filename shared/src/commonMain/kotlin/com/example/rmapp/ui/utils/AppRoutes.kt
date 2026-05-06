package com.example.rmapp.ui.utils

object AppRoutes {
    const val INVENTORY = "inventory"
    const val PRODUCTS = "products"
    const val CUSTOMERS = "customers"
    const val STOCK = "stock"

    const val PRODUCTS_FORM = "products_form"
    const val PRODUCTS_FORM_WITH_ID = "products_form/{productId}"
    fun productFormWithId(id: String) = "products_form/$id"

    const val CUSTOMERS_FORM = "customers_form"
    const val CUSTOMERS_FORM_WITH_ID = "customers_form/{customerId}"
    fun customerFormWithId(id: String) = "customers_form/$id"

    const val INVENTORY_FORM = "inventory_form"
    const val INVENTORY_FORM_WITH_ID = "inventory_form/{inventoryId}"
    fun inventoryFormWithId(id: String) = "inventory_form/$id"

}