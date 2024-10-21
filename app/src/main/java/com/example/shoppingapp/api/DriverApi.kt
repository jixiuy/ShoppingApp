package com.example.shoppingapp.api

import com.example.shoppingapp.models.DriverBean
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface DriverApi {
    @GET("vehicle/products")
    suspend fun getCarInformation(
        @Header("token") token: String
    ): Response<DriverBean>

}