package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.models.CarInformationResponse
import com.example.shoppingapp.models.LoginResponse
import com.example.shoppingapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PassengerViewModel : ViewModel() {
    private val userRepository = UserRepository()

    var license: MutableState<String> = mutableStateOf("")

    // 使用 StateFlow 来管理登录响应
    private val _carInformationResponse = MutableStateFlow<CarInformationResponse?>(null)
    val carInformationResponse: StateFlow<CarInformationResponse?> = _carInformationResponse

    suspend fun getCarInformation(license:Int,token:String){
        viewModelScope.launch {
            val response = userRepository.getCarInformation(license,token)
            _carInformationResponse.value = response
        }

    }
}