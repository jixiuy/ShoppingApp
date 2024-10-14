package com.example.shoppingapp.repository

import com.example.shoppingapp.models.LoginResponse
import com.example.shoppingapp.network.RetrofitClient

class UserRepository {
    private val apiService = RetrofitClient.loginApiService

    suspend fun login(phone: String, password: String): LoginResponse {
        return apiService.login(phone, password)
    }
}