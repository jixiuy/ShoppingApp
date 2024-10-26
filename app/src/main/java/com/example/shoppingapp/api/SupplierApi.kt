package com.example.shoppingapp.api

import com.example.shoppingapp.models.RequestResponse
import com.example.shoppingapp.models.StationBean
import com.example.shoppingapp.models.StationInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

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

}