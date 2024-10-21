package com.example.shoppingapp

import android.app.Application
import android.content.Context
import com.example.shoppingapp.viewmodel.LoginViewModel
import com.example.shoppingapp.viewmodel.LoginViewModelFactory

// 创建一个 Application 级别的 ViewModel
class MyApp : Application() {
    val loginViewModel: LoginViewModel by lazy {
        LoginViewModelFactory(this).create(LoginViewModel::class.java)
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        // 静态变量
        var count: Int? = 0
        var ToastCarInfoOnce:Int? = 0
        private lateinit var instance: MyApp

        fun getContext(): Context {
            return instance.applicationContext
        }
    }
}


