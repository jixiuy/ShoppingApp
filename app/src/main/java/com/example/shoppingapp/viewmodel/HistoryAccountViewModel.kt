package com.example.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.models.HistoryAccountResponse
import com.example.shoppingapp.models.LoginResponse
import com.example.shoppingapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryAccountViewModel : ViewModel()  {
    private val userRepository = UserRepository()

    // 使用 StateFlow 来管理登录响应
    private val _historyAccountResponse = MutableStateFlow<HistoryAccountResponse?>(null)
    val historyAccountResponse: StateFlow<HistoryAccountResponse?> = _historyAccountResponse

    suspend fun historyAccount(token:String){
        viewModelScope.launch {
            val response = userRepository.historyAccount(token)
            _historyAccountResponse.value = response
        }
    }
}