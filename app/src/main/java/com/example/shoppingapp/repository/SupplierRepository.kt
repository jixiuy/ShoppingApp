package com.example.shoppingapp.repository

import com.example.shoppingapp.models.StationBean
import com.example.shoppingapp.network.RetrofitClient
import retrofit2.Response

class SupplierRepository {

    private val supplierServer = RetrofitClient.supplierApiService

    suspend fun getStationShoppingInfo(token:String):Response<StationBean>{
        return supplierServer.getCarInformation(token)
    }
}