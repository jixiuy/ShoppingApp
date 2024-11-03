package com.example.shoppingapp.models

data class ApiResponse(
    val code: Int,
    val data: Any?,
    val message: String?
)