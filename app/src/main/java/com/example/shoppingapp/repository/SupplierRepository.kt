package com.example.shoppingapp.repository

import com.example.shoppingapp.models.DriverBean
import com.example.shoppingapp.models.SupplierBean
import com.example.shoppingapp.network.RetrofitClient
import retrofit2.Response

class SupplierRepository {

    private val supplierServer = RetrofitClient.supplierApiService

    suspend fun getSupplierStationShoppingInfo(token:String):Response<SupplierBean>{
        return supplierServer.getCarInformation(token)
    }
}