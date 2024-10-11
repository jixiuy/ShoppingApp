package com.example.shoppingapp.repository

import com.example.shoppingapp.api.ProductApi
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository {
    private val productApi = RetrofitClient.productApiService.create(ProductApi::class.java)

    fun listProducts(driverId: String, page: Int, size: Int, callback: (Result<List<Product>>?) -> Unit) {
        productApi.listProducts(driverId, page, size).enqueue(object :
            Callback<Result<List<Product>>> {
            override fun onResponse(call: Call<Result<List<Product>>>, response: Response<Result<List<Product>>>) {
                callback(response.body())
            }

            override fun onFailure(call: Call<Result<List<Product>>>, t: Throwable) {
                callback(null)
            }
        })

    }

    fun getProductDetails(productId: String, callback: (Result<Product>?) -> Unit) {
        productApi.getProductDetails(productId).enqueue(object : Callback<Result<Product>> {
            override fun onResponse(call: Call<Result<Product>>, response: Response<Result<Product>>) {
                callback(response.body())
            }

            override fun onFailure(call: Call<Result<Product>>, t: Throwable) {
                callback(null)
            }
        })
    }


}