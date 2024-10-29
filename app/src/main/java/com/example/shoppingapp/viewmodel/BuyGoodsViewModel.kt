package com.example.shoppingapp.viewmodel

import ToastUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.MyApp
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.models.ProductResponse
import com.example.shoppingapp.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuyGoodsViewModel : ViewModel() {
    private val _products = MutableLiveData<List<ProductResponse.Product>>()
    val products: LiveData<List<ProductResponse.Product>> get() = _products



     fun fetchProducts(productName: String, token: String) {
        viewModelScope.launch {
            val apiService = RetrofitClient.goodsApiService.getProductByName(productName,token)
            if(apiService.isSuccessful&&apiService.body()?.code==200){
                _products.postValue(apiService.body()!!.data)
            }else{
                if(token==null){
                    ToastUtil.showCustomToast(MyApp.getContext(),"请先登录")
                }else ToastUtil.showCustomToast(MyApp.getContext(),"商品信息未查询到")
            }
        }
    }
     fun addVehicleProduct(productId: Int, token: String) {
        viewModelScope.launch {
            val apiService = RetrofitClient.goodsApiService.addVehicleProduct(productId,token)
            if(apiService.isSuccessful&&apiService.body()?.code==200){
                ToastUtil.showCustomToast(MyApp.getContext(),"添加车载成功")
            }else{
                ToastUtil.showCustomToast(MyApp.getContext(),"添加车载失败")
            }
        }
    }

    fun addReplenishmentStationProduct(productId: Int, token: String) {
        viewModelScope.launch {
            val apiService = RetrofitClient.goodsApiService.addReplenishmentStationProduct(productId,token)
            if(apiService.isSuccessful&&apiService.body()?.code==200){
                ToastUtil.showCustomToast(MyApp.getContext(),"添加供货站商品成功")
            }else{
                ToastUtil.showCustomToast(MyApp.getContext(),"添加供货站商品失败")
            }
        }
    }
}
