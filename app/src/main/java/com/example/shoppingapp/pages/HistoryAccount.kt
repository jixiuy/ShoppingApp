package com.example.shoppingapp.pages

import ToastUtil
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.MyApp
import com.example.shoppingapp.R
import com.example.shoppingapp.models.HistoryAccountResponse
import com.example.shoppingapp.viewmodel.HistoryAccountViewModel
import com.example.shoppingapp.viewmodel.LoginViewModel
import kotlinx.coroutines.launch


class HistoryAccount : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HistoryOrdersScreen()
        }
    }

    private val historyAccountViewModel: HistoryAccountViewModel by viewModels()


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HistoryOrdersScreen() {
        val historyAccountResponse by historyAccountViewModel.historyAccountResponse.collectAsState()

        var isShow by remember { mutableStateOf(false) }
        val context = LocalContext.current
        // 使用 LaunchedEffect 触发 ViewModel 的网络请求
        LaunchedEffect(GlobalToken.token) {
            if(GlobalToken.token==null){
                ToastUtil.showCustomToast(context,"请先登录",R.drawable.icon)
            }else historyAccountViewModel.historyAccount(GlobalToken.token.toString())
        }

        // 当 historyAccountResponse 成功返回并且 code 为 200 时，显示数据
        if (historyAccountResponse != null && historyAccountResponse?.code == 200) {
            isShow = true
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("历史订单", fontSize = 20.sp) },
                    navigationIcon = {
                        IconButton(onClick = { (context as HistoryAccount).finish() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "返回"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (isShow) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                ) {
                    items(historyAccountResponse?.data ?: emptyList()) { order ->
                        OrderItem(order)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun OrderItem(order: HistoryAccountResponse.Order) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable(
                    onClick = { /* 点击事件 */ },
                    indication = rememberRipple(), // 添加水波纹效果
                    interactionSource = remember { MutableInteractionSource() }
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(data = order.imageUrl),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(start = 10.dp)
                )

                MyText(order)
            }
        }
    }

    @Composable
    fun MyText(order: HistoryAccountResponse.Order){
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "商品名称: ${order.productName}",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "总数量: ${order.sum}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "总金额: $${order.totalAmount}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }


}
