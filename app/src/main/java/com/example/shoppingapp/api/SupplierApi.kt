package com.example.shoppingapp.api

import com.example.shoppingapp.models.StationBean
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface SupplierApi {
    @GET("replenishmentStation/products")
    suspend fun getCarInformation(
        @Header("token") token: String
    ): Response<StationBean>

}