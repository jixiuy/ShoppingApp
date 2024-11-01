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

    suspend fun fetchProducts(stationId: Int, token: String): Result<List<com.example.shoppingapp.models.StationProductResponse.Product>> {
        return try {
            val response = supplierServer.getProducts(stationId, token)
            if (response.isSuccessful && response.body()?.code == 200) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception("获取产品列表失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}