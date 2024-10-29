package com.example.shoppingapp.repository

import com.example.shoppingapp.models.StationBean
import com.example.shoppingapp.models.StationInfoResponse
import com.example.shoppingapp.models.StationShoppingResponse
import com.example.shoppingapp.network.RetrofitClient
import retrofit2.Response

class SupplierRepository {

    private val supplierServer = RetrofitClient.supplierApiService

    suspend fun getStationShoppingInfo(token:String):Response<StationBean>{
        return supplierServer.getCarInformation(token)
    }
    suspend fun getStationInfo(token:String):Response<StationInfoResponse>{
        return supplierServer.getStationInfo(token)
    }


    suspend fun incrementProduct(productId: Int, num: Int,token:String): Response<StationShoppingResponse> {
        return supplierServer.incrementProduct(productId,num,token)
    }


    suspend fun decrementProduct(productId: Int, num: Int,token:String):Response<StationShoppingResponse>{
        return supplierServer.decrementProduct(productId,num,token)
    }

    suspend fun storeProduct(productId: Int, num: Int,token:String):Response<StationShoppingResponse>{
        return supplierServer.storeProduct(productId,num,token)
    }

    suspend fun deleteProductInfo(productId: Int,token:String):Response<StationShoppingResponse>{
        return supplierServer.deleteProductInfo(productId , token)
    }

}