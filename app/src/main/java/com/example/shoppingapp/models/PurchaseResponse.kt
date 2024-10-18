package com.example.shoppingapp.models

data class PurchaseResponse(
    val code: Int,
    val data: PurchaseData?,
    val message: String?
)

data class PurchaseData(
    val orderId: Int,
    val userId: String,
    val productId: Int,
    val totalAmount: Double,
    val sum: Int,
    val latitude: Double,
    val longitude: Double
)
