package com.example.shoppingapp.models

data class Product(
    val id: String,
    val name: String,
    val price: Double
)

data class Order(
    val id: String,
    val userId: String,
    val products: List<Product>
)