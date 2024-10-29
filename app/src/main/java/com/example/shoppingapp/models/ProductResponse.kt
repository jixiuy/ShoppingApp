package com.example.shoppingapp.models
data class ProductResponse(
    val code: Int,
    val data: List<Product>?,
    val message: String?
){
    data class Product(
        val id: Int,
        val name: String,
        val descInfo: String,
        val price: Double,
        val imgUrl: String,
        val type: String
    )

}

