package com.example.shoppingapp.pages

import MyProfilePage
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import com.example.shoppingapp.MyApp
import com.example.shoppingapp.R
import com.example.shoppingapp.ui.theme.ShoppingAppTheme
import com.example.shoppingapp.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

val LocalLoginViewModel = compositionLocalOf<LoginViewModel> {
    error("No ViewModel provided")
}
class MainActivity : ComponentActivity() {
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 在 Activity1 和 Activity2 中访问
        loginViewModel = (application as MyApp).loginViewModel

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )


        setContent {
            ShoppingAppTheme {
                CompositionLocalProvider(LocalLoginViewModel provides loginViewModel) {
                    TransparentStatusBar()
                }
            }
        }
    }

    @Composable
    fun TransparentStatusBar() {
        val resources = LocalContext.current.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
        val statusBarPadding = with(LocalDensity.current) { statusBarHeight.toDp() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEFEEF6))
                .padding(top = statusBarPadding) // 添加状态栏高度的填充
        ) {
            // 你的页面内容
            driverOrSupplier()
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
                        1 -> DriverGoodsPage()
                        2 -> RestockRequestPage()
                        3 -> MyProfilePage()
                    }
                }
            }
        }
    }


}



