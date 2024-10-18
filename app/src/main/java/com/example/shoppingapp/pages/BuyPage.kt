package com.example.shoppingapp.pages

import ToastUtil
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.MyApp
import com.example.shoppingapp.R
import com.example.shoppingapp.models.CarInformationResponse
import com.example.shoppingapp.viewmodel.PassengerViewModel
import com.example.shoppingapp.viewmodel.ProductViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyPage() {
    val passengerViewModel: PassengerViewModel = viewModel()
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    var isShow by remember {
        mutableStateOf(false)
    }


    val carResponse by passengerViewModel.carInformationResponse.collectAsState(initial = null)

    carResponse?.let { response ->
        if (response.code == 200) {
            isShow = true
            if (MyApp.ToastCarInfoOnce == 0) {
                ToastUtil.showCustomToast(context, "输入成功", R.drawable.icon)

                MyApp.ToastCarInfoOnce = 1
            }

        } else {
            ToastUtil.showCustomToast(context, "请输入正确的车牌号", R.drawable.icon)
        }
    }
    if (!isShow) {
        inputCarLicense(coroutineScope, passengerViewModel, context)
    }
    if (isShow) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("车牌号：${passengerViewModel.license.value}", fontSize = 20.sp) },
                )
            }
        ) { paddingValues ->

            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
            ) {
                items(carResponse?.data ?: emptyList()) { infor ->
                    CarItem(infor,passengerViewModel.license.value)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }


    }

}

@Composable
fun CarItem(infor: CarInformationResponse.Data, value: String) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val productViewModel: ProductViewModel = viewModel()
    // 显示对话框输入信息
    if (showDialog) {
        TextInputDialog(onDismiss = { showDialog = false }, onConfirm = { count ->

            coroutineScope.launch {
                if (GlobalToken.token == null) {
                    ToastUtil.showCustomToast(context, "请先登录", R.drawable.icon)
                } else {
                    Log.d("purchase111", "CarItem: ${infor.id},${count.toInt()},${value}")
                    productViewModel.purchaseProduct(context,infor.id,count.toInt(),value.toInt(),GlobalToken.token.toString())

                }

            }
            showDialog = false
        })
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(
                onClick = {
                    showDialog = true
                },
                indication = rememberRipple(), // 添加水波纹效果
                interactionSource = remember { MutableInteractionSource() }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)
        ) {
            Image(
                painter = rememberImagePainter(
                    data = infor.imgUrl,
                ),
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(80.dp)
                    .padding(start = 5.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            MyText(infor)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputDialog(
    onDismiss: () -> Unit, onConfirm: (String) -> Unit // 用于返回两个文本输入的内容
) {
    var secondText by remember { mutableStateOf("") }


    AlertDialog(onDismissRequest = onDismiss, title = { Text("输入需要购买的商品数量") }, text = {
        Column {
            // 第二个文本框，输入数量
            OutlinedTextField(
                value = secondText,
                onValueChange = { secondText = it },
                label = { Text("数量") },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }, confirmButton = {
        TextButton(onClick = {
            onConfirm(secondText)
            onDismiss()
        }) {
            Text("确认")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("取消")
        }
    }, properties = DialogProperties(dismissOnClickOutside = false)
    )
}


@Composable
fun MyText(order: CarInformationResponse.Data) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "商品名称: ${order.name}",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "价格: $${order.price}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "详细信息: ${order.descInfo}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun inputCarLicense(
    coroutineScope: CoroutineScope,
    passengerViewModel: PassengerViewModel,
    context: Context
) {
    var license by remember {
        mutableStateOf("")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = -140.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 欢迎文本和图标

            // 账号输入框
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                OutlinedTextField(
                    value = passengerViewModel.license.value, // 使用ViewModel中的license
                    onValueChange = {
                        passengerViewModel.license.value = it // 更新ViewModel中的license
                         },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    placeholder = { Text(text = "请输入司机的车牌号") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF515D87),  // 获得焦点时的边框颜色
                    ),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                )
            }

            // 登录按钮
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (GlobalToken.token != null) {
                            passengerViewModel.getCarInformation(
                                passengerViewModel.license.value.toInt(),
                                GlobalToken.token.toString()
                            )

                        } else {
                            ToastUtil.showCustomToast(context, "请先登录", R.drawable.icon)
                        }

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp), // 设置按钮高度
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEFEEF6),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "确定",
                    color = Color(0xFF515D87),
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }
    }
}

