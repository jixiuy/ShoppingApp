package com.example.shoppingapp.api

import com.example.shoppingapp.models.ProductResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface BuyGoodsApi {
    @POST("product/getByName")
    suspend fun getProductByName(
        @Query("productName") productName: String,
        @Header("token") token: String
    ): Response<ProductResponse>

    @POST("vehicle/product/add")
    suspend fun addVehicleProduct(
        @Query("productId") productId: Int,
        @Header("token") token: String
    ): Response<com.example.shoppingapp.models.Response>

    @POST("replenishmentStation/products")
    suspend fun addReplenishmentStationProduct(
        @Query("productId") productId: Int,
        @Header("token") token: String
    ): Response<com.example.shoppingapp.models.Response>
}