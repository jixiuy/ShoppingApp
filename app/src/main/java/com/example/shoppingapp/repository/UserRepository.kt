package com.example.shoppingapp.repository

import com.example.shoppingapp.models.CarInformationResponse
import com.example.shoppingapp.models.DriverResult
import com.example.shoppingapp.models.HistoryAccountResponse
import com.example.shoppingapp.models.LoginResponse
import com.example.shoppingapp.models.RegisterResponse
import com.example.shoppingapp.models.StationResponse
import com.example.shoppingapp.models.User
import com.example.shoppingapp.network.RetrofitClient
import com.example.shoppingapp.pages.HistoryAccount
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val loginService = RetrofitClient.loginApiService

    private val passengerService = RetrofitClient.passengerApiService

    private val registerService = RetrofitClient.registerService

    suspend fun register(user: User):Response<RegisterResponse>{
        return registerService.registerUser(user)
    }

    suspend fun login(phone: String, password: String): LoginResponse {
        return loginService.login(phone, password)
    }

    suspend fun registerDriver(license: String, location: String, token: String): Response<DriverResult> {
        return passengerService.registerDriver(license, location, token)
    }

    suspend fun historyAccount(token:String): HistoryAccountResponse? {
        return passengerService.historyAccount(token)
    }


    suspend fun registerStation(
        storeName: String,
        contactInfo: String,
        addressDetails: String,
        stationType: String,
        token: String
    ): Response<StationResponse> {
        return passengerService.registerStation(storeName, contactInfo, addressDetails, stationType, token)
    }

    suspend fun getCarInformation(
        license:Int,
        token:String
    ): CarInformationResponse {
        return passengerService.getCarInformation(license,token)
    }

}