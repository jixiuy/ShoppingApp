package com.example.shoppingapp.api

import com.example.shoppingapp.models.RequestResponse
import com.example.shoppingapp.models.StationBean
import com.example.shoppingapp.models.StationInfoResponse
import com.example.shoppingapp.models.StationProductResponse
import com.example.shoppingapp.models.StationShoppingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SupplierApi {
    @GET("replenishmentStation/products")
    suspend fun getCarInformation(
        @Header("token") token: String
    ): Response<StationBean>

    @GET("replenishmentStation/stationList")
    suspend fun getStationInfo(
        @Header("token") token: String
    ): Response<StationInfoResponse>

    @GET("replenishmentStation/replenishment/findRequests")
    suspend fun getRequests(
        @Header("token") token: String
    ): Response<RequestResponse>

    @POST("replenishmentStation/products/inc")
    suspend fun incrementProduct(
        @Query("productId") productId: Int,
        @Query("num") num: Int,
        @Header("token") token: String
    ): Response<StationShoppingResponse>

    @POST("replenishmentStation/products/dec")
    suspend fun decrementProduct(
        @Query("productId") productId: Int,
        @Query("num") num: Int,
        @Header("token") token: String
    ): Response<StationShoppingResponse>

    @POST("replenishmentStation/products/store")
    suspend fun storeProduct(
        @Query("productId") productId: Int,
        @Query("num") num: Int,
        @Header("token") token: String
    ): Response<StationShoppingResponse>

    @POST("replenishmentStation/products/{productId}")
    suspend fun deleteProductInfo(
        @Path("productId") productId: Int,
        @Header("token") token: String
    ): Response<StationShoppingResponse>

    @POST("replenishmentStation/request/prodcuts/{stationId}")
    suspend fun getProducts(
        @Path("stationId") stationId: Int,
        @Header("token") token: String
    ): Response<StationProductResponse>
}