package com.example.shoppingapp.models

data class HistoryAccountResponse(
    val code:Int?,
    val data: List<Order>,
    val message:String?
){
    data class Order(
        val orderId: Int,
        val userId: String,
        val productName: String,
        val imageUrl: String,
        val totalAmount: Double,
        val sum: Int
    )

}
