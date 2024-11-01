package com.example.shoppingapp.repository

import com.example.shoppingapp.models.CarShoppingModifyResponse
import com.example.shoppingapp.models.DriverBean
import com.example.shoppingapp.models.RequestBody
import com.example.shoppingapp.network.RetrofitClient
import retrofit2.Response

class DriverRepository {
    private val driverService = RetrofitClient.driverApiService

    suspend fun getCarShoppingInfo(token:String):Response<DriverBean>{
        return driverService.getCarInformation(token)
    }

    suspend fun deleteCarShoppingInfo(productId:Int,token:String): Response<CarShoppingModifyResponse> {
        return driverService.deleteStock(productId,token)
    }
    suspend fun sendRequest(stationId: Int, token: String,vehicle: RequestBody.VehicleRequest): Result<CarShoppingModifyResponse> {
        return try {
            val response = driverService.sendRequest(stationId, token,vehicle)
            // 检查响应是否成功且返回代码为200
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.code == 200) {
                    Result.success(body) // 返回整个响应体
                } else {
                    Result.failure(Exception("获取产品列表失败: ${body?.message ?: "未知错误"}"))
                }
            } else {
                Result.failure(Exception("网络请求失败: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e) // 直接返回异常
        }
    }


}