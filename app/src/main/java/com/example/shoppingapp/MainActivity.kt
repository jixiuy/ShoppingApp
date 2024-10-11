package com.example.shoppingapp

import MyProfilePage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.shoppingapp.pages.BuyPage
import com.example.shoppingapp.pages.MyProductsPage
import com.example.shoppingapp.pages.RestockRequestPage
import com.example.shoppingapp.ui.theme.ShoppingAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingAppTheme {
                // A surface container using the 'background' color from the theme
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
                                3 -> MyProfilePage()
                            }
                        }
                    }
                }
            }
        }
    }
}
