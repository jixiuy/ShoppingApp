package com.example.shoppingapp.models

data class User(
    val nickName: String,
    val realName: String,
    val userMobile: String,
    val sex: String,
    val password: String
)

data class RegisterResponse(
    val code: Int,
    val message: String,
    val data: String
)