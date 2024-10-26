package com.example.shoppingapp.api

import com.example.shoppingapp.models.CarShoppingModifyResponse
import com.example.shoppingapp.models.DriverBean
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DriverApi {
    @GET("vehicle/products")
    suspend fun getCarInformation(
        @Header("token") token: String
    ): Response<DriverBean>

    @POST("vehicle/product/inc/{productId}")
    fun increaseStock(
        @Path("productId") productId: Int,
        @Query("num") num: Int,
        @Header("token") token: String
    ): Call<CarShoppingModifyResponse>

    @POST("vehicle/product/dec/{productId}")
    fun decreaseStock(
        @Path("productId") productId: Int,
        @Query("num") num: Int,
        @Header("token") token: String
    ): Call<CarShoppingModifyResponse>

    @POST("vehicle/product/change")
    fun changeStock(
        @Query("productId") productId: Int,
        @Query("num") num: Int,
        @Header("token") token: String
    ): Call<CarShoppingModifyResponse>

    @POST("vehicle/product/delete/{productId}")
    suspend fun deleteStock(
        @Path("productId") productId:Int,
        @Header("token") token:String
    ):Response<CarShoppingModifyResponse>
}