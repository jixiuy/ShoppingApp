package com.example.shoppingapp.models

data class LoginResponse(
    val code: Int,
    val data: Data?,
    val message: String?
)

data class Data(
    val id: String,
    val username: String,
    val role: Int,
    val token: String
)