package com.example.shoppingapp.models

data class StationInfoResponse(
    val code: Int,
    val data: List<Station>,
    val message: String?
)
data class Station(
    val stationId: Int,
    val storeName: String,
    val contactInfo: String,
    val addressDetails: String,
    val stationType: String,
    val stationStatus: String,
    val latitude: Double,
    val longitude: Double,
    val userId: String
)
