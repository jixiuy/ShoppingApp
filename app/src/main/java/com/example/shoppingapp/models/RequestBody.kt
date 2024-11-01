package com.example.shoppingapp.models


class RequestBody(val vehicles: List<VehicleRequest>) {

    class VehicleRequest(private val id: Int, private val quantity: Int)
}
