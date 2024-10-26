package com.example.shoppingapp.models

data class RequestResponse(
    val code: Int,
    val data: List<UserRequest>,
    val message: String?
)

data class UserRequest(
    val username: String,
    val phone: String,
    val requestProductInfoDTO: List<ProductInfo>
)

data class ProductInfo(
    val id: Int,
    val productName: String,
    val quantity: Int
)
