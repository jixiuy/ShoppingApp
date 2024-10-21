package com.example.shoppingapp.models

data class SupplierBean(
    val code:Int,
    val message:String?,
    val data:List<Data>?
){
    data class Data(
        val id:Int,
        val name:String,
        val descInfo:String,
        val price:Double,
        val imgUrl:String,
        val type:String,
        //val num:Int
    )
}