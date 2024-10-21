package com.example.shoppingapp.network

import PassengerApi
import com.example.shoppingapp.api.DriverApi
import com.example.shoppingapp.api.LoginApi
import com.example.shoppingapp.api.SupplierApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://47.101.61.119:8888/"

    val productApiService: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val passengerApiService: PassengerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PassengerApi::class.java)
    }

    val loginApiService: LoginApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginApi::class.java)
    }

    val driverApiService: DriverApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DriverApi::class.java)
    }

    val supplierApiService: SupplierApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupplierApi::class.java)
    }
}