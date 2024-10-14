package com.example.shoppingapp.api

import com.example.shoppingapp.models.LoginResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApi {
    @POST("user/login")
    suspend fun login(
        @Query("phone") phone: String,
        @Query("password") password: String
    ): LoginResponse
}