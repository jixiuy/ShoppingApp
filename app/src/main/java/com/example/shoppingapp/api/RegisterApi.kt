package com.example.shoppingapp.api

import com.example.shoppingapp.models.RegisterResponse
import com.example.shoppingapp.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
    @POST("user/regist")
    suspend fun registerUser(@Body user: User): Response<RegisterResponse> // 返回注册结果
}