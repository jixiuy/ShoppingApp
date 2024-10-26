package com.example.shoppingapp.viewmodel

import ToastUtil
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.R
import com.example.shoppingapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    fun purchaseProduct(
        context: Context,
        productId: Int,
        quantity: Int,
        license: Int,
        token: String
    ) {

        viewModelScope.launch {
            try {
                val response = RetrofitClient.passengerApiService.purchaseProduct(
                    productId,
                    quantity,
                    license,
                    token
                )
                Log.d("purchase111", "CarItem: ${response.body()}")
                if (response.body()?.code==200) {
                    val purchaseResponse = response.body()
                    purchaseResponse?.data?.let { data ->
                        ToastUtil.showCustomToast(context, "购买商品成功", R.drawable.icon)
                    }
                    //ToastUtil.showCustomToast(context, "别", R.drawable.icon)
                } // 处理非2xx的响应状态码，比如400等
                else if(response.body()?.code==400){
                    val errorResponse = response.body()

                    ToastUtil.showCustomToast(context, errorResponse?.message.toString(), R.drawable.icon)

                }
            } catch (e: Exception) {
                ToastUtil.showCustomToast(context, "网络请求发生异常", R.drawable.icon)
            }
        }
    }
}
