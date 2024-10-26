package com.example.shoppingapp.repository

import com.example.shoppingapp.models.DriverBean
import com.example.shoppingapp.models.LoginResponse
import com.example.shoppingapp.network.RetrofitClient
import retrofit2.Response

class DriverRepository {
    private val driverService = RetrofitClient.driverApiService

    suspend fun getCarShoppingInfo(token:String):Response<DriverBean>{
        return driverService.getCarInformation(token)
    }



}