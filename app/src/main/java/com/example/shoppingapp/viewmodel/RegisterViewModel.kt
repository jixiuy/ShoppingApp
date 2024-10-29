package com.example.shoppingapp.viewmodel

import ToastUtil
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.MyApp
import com.example.shoppingapp.R
import com.example.shoppingapp.models.User
import com.example.shoppingapp.network.RetrofitClient
import com.example.shoppingapp.repository.SupplierRepository
import com.example.shoppingapp.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class RegisterViewModel : ViewModel() {

    private val registerRepository = UserRepository()

    private val _registerStatus = MutableLiveData(false)
    val registerStatus: LiveData<Boolean> get() = _registerStatus
    fun register(user: User) {
        viewModelScope.launch {
            try {
                // 发起网络请求，直接获得 Response 对象
                val response = registerRepository.register(user)

                if (response.isSuccessful && response.body()?.code == 200) {
                    // 注册成功
                    ToastUtil.showCustomToast(MyApp.getContext(), "注册成功", R.drawable.icon)
                    _registerStatus.postValue(true)
                } else {
                    // 注册失败，处理错误信息
                    ToastUtil.showCustomToast(MyApp.getContext(), "${response.body()?.message}", R.drawable.icon)
                    _registerStatus.postValue(false)
                }
            } catch (e: Exception) {
                // 处理网络异常
                ToastUtil.showCustomToast(MyApp.getContext(), "发生异常: ${e.message}", R.drawable.icon)
            }
        }
    }


}