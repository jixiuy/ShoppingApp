package com.example.shoppingapp

import MyProfilePage
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.shoppingapp.pages.BuyPage
import com.example.shoppingapp.pages.MyProductsPage
import com.example.shoppingapp.pages.RestockRequestPage
import com.example.shoppingapp.ui.theme.ShoppingAppTheme
import com.example.shoppingapp.viewmodel.LoginViewModel
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 创建LoginViewModel
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setContent {
            ShoppingAppTheme {
                LoginScreen()
            }
        }
    }

    private lateinit var loginViewModel: LoginViewModel

    @Composable
    fun LoginScreen() {
        var isLoading by remember { mutableStateOf(true) }
        var loginSuccess by remember { mutableStateOf(false) }
        val context = LocalContext.current

        // 从 ViewModel 中获取状态
        val loginResponse by loginViewModel.loginResponse

        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp)
                )

                // 登录并观察结果
                LaunchedEffect(Unit) {
                    loginViewModel.login("13800000004", "123456")
                }
            } else {
                // 根据 loginResponse 更新UI
                if (loginSuccess) {

                    ToastUtil.showCustomToast(
                        context = context,
                        message = "登录成功",
                        iconResId = R.drawable.icon // 替换为你的图标资源
                    )
                    driverOrSupplier()
                } else {
                    Text(
                        text = "登录失败，请重试。",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clickable {
                                isLoading = true
                                loginSuccess = false
                                loginViewModel.login("13800000004", "123456")
                            }
                    )
                }
            }

            // 当 loginResponse 更新时进行状态更新
            loginResponse?.let { response ->
                if (response.code == 200) {
                    GlobalToken.token = response.data?.token
                    Log.d("登录成功", "LoginScreen: ${response}")
                    isLoading = false
                    loginSuccess = true
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "登录失败：${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("登录失败", "LoginScreen: ${response}")
                    isLoading = false
                }
            }
        }

    }


    @Composable
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    private fun driverOrSupplier() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var selectedItem by remember { mutableStateOf(0) }
            val items = listOf("购买商品", "我的商品", "补货请求", "个人中心")
            val pagerState = rememberPagerState()
            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(pagerState.currentPage) {
                selectedItem = pagerState.currentPage
            }
            Scaffold(
                bottomBar = {
                    NavigationBar() {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                icon = {
                                    when (index) {
                                        0 -> Icon(
                                            painter = painterResource(id = R.drawable.baseline_add_shopping_cart_24),
                                            contentDescription = null
                                        )

                                        1 -> Icon(
                                            Icons.Filled.ShoppingCart,
                                            contentDescription = null
                                        )

                                        2 -> Icon(
                                            Icons.Filled.Call,
                                            contentDescription = null
                                        )

                                        else -> Icon(
                                            Icons.Filled.Person,
                                            contentDescription = null
                                        )
                                    }
                                },
                                label = { Text(item) },
                                selected = selectedItem == index,
                                onClick = {
                                    selectedItem = index
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                HorizontalPager(
                    pageCount = items.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) { page ->
                    when (page) {
                        0 -> BuyPage()
                        1 -> MyProductsPage()
                        2 -> RestockRequestPage()
                        3 -> MyProfilePage(loginViewModel = loginViewModel)
                    }
                }
            }
        }
    }

    @Composable
    private fun passenger(){

    }

}
