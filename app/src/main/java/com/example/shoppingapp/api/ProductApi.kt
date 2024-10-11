package com.example.shoppingapp.api

import com.example.shoppingapp.models.BuyProductRequest
import com.example.shoppingapp.models.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ProductApi {

    @GET("addition/products")
    fun listProducts(
        @Query("driverId") driverId: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<Result<List<Product>>>

    @GET("addition/product/{productId}")
    fun getProductDetails(@Path("productId") productId: String): Call<Result<Product>>

    @POST("addition/product")
    fun addProduct(@Body product: Product): Call<Result<Void>>

    @PUT("addition/product")
    fun updateProduct(@Body product: Product): Call<Result<Void>>

    @DELETE("addition/product/{productId}")
    fun deleteProduct(@Path("productId") productId: String): Call<Result<Void>>

    // 购买商品
    @POST("addition/buy")
    fun buyProduct(
        @Body buyProductRequest: BuyProductRequest,
        @Query("price") price: Double,
        @Query("amount") amount: Int,
        @Query("x") x: String,
        @Query("y") y: String
    ): Call<com.example.shoppingapp.models.Result>

    // 列出用户订单
    @GET("addition/user/orders")
    fun listUserOrders(
        @Query("userId") userId: String
    ): Call<com.example.shoppingapp.models.Result>

    // 获取订单详情
    @GET("addition/order/{orderId}")
    fun getOrderDetails(
        @Path("orderId") orderId: String
    ): Call<com.example.shoppingapp.models.Result>

}