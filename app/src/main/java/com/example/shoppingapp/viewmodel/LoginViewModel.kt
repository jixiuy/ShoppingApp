package com.example.shoppingapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.R
import com.example.shoppingapp.models.LoginResponse
import com.example.shoppingapp.repository.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository()

    // 使用 StateFlow 来管理登录响应
    private val _loginResponse = MutableStateFlow<LoginResponse?>(null)
    val loginResponse: StateFlow<LoginResponse?> = _loginResponse

    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    suspend fun login(phone: String, password: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.login(phone, password)
                Log.d("Header1111", "login: ${response}")
                _loginResponse.value = response
                Log.d("Header1111", "_loginResponse: ${_loginResponse.value}")
                _isLoggedIn.postValue(true) // 登录成功，设置为true
            } catch (e: Exception) {
                Log.e("ViewModel", "Network request failed: ${e.message}")
                _isLoggedIn.postValue(false) // 发生异常，登录失败
            }
        }
    }

    // 清除登录状态（可用于登出）
    fun logout() {
        _isLoggedIn.value = false
        GlobalToken.token = null
    }

}
