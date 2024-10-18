package com.example.shoppingapp

import android.app.Application
import com.example.shoppingapp.viewmodel.LoginViewModel
import com.example.shoppingapp.viewmodel.LoginViewModelFactory

// 创建一个 Application 级别的 ViewModel
class MyApp : Application() {
    val loginViewModel: LoginViewModel by lazy {
        LoginViewModelFactory(this).create(LoginViewModel::class.java)
    }

    companion object {
        // 静态变量
        var count: Int? = 0
        var ToastCarInfoOnce:Int? = 0
    }
}


