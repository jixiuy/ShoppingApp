package com.example.shoppingapp.models

// 数据模型

data class StationProductResponse(
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
        val type: String,
        val num: Int,
        val replenishmentProductId: Any?
    )

}