package com.example.shoppingapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.models.LoginResponse
import com.example.shoppingapp.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _loginResponse = mutableStateOf<LoginResponse?>(null)
    val loginResponse: State<LoginResponse?> = _loginResponse

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            // 模拟网络请求
            val response = userRepository.login(phone, password)
            _loginResponse.value = response
        }
    }



}