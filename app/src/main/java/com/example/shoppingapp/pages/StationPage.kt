package com.example.shoppingapp.pages

import StationViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.MyApp
import com.example.shoppingapp.R
import com.example.shoppingapp.models.Station
import com.example.shoppingapp.models.UserRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationPage() {
    val stationViewModel: StationViewModel = viewModel()

    val stationList = stationViewModel.stationList.observeAsState()
    val requestList = stationViewModel.requestList.observeAsState()

    val loginViewModel = LocalLoginViewModel.current

    val isLoggedIn by loginViewModel.isLoggedIn.observeAsState(false)
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn ) {
            // 如果已经登录并且 stationList 为空，发起数据请求
            GlobalToken.token?.let { stationViewModel.getStationInfo(it) }
            GlobalToken.token?.let { stationViewModel.getRequests(it) }
        }

    }
    val navController = rememberNavController() // 创建 NavController

    NavHost(navController = navController, startDestination = "stationPage") {
        composable("stationPage") {
            FirstPage(stationList, requestList,navController)
        }
        composable("target_screen/{stationId}/{stationName}") { backStackEntry ->
            // 获取参数时，首先是 String 类型
            val stationIdString = backStackEntry.arguments?.getString("stationId")
            val stationName = backStackEntry.arguments?.getString("stationName")

            // 将 String 转换为 Int
            val stationId = stationIdString?.toIntOrNull() // 使用 toIntOrNull 安全转换

            // 确保 stationId 和 stationName 都不为 null
            if (stationId != null && stationName != null) {
                StationGoodsPage(stationId, stationName, navController)
            }
        }
    }




}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FirstPage(

    stationList: State<List<Station>?>,
    requestList: State<List<UserRequest>?>,
    navController:NavController
) {
    var flagCharge by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    if (!flagCharge) Text("供货站信息", fontSize = 20.sp)
                    else Text("查看申请信息", fontSize = 20.sp)

                }, actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "切换")
                        IconButton(onClick = { flagCharge = !flagCharge }) {
                            if (!flagCharge) Icon(
                                painter = painterResource(id = R.drawable.baseline_toggle_off_24),
                                contentDescription = "切换",
                                //tint = Color(0xFFEFEEF6),
                            )
                            else Icon(
                                painter = painterResource(id = R.drawable.baseline_toggle_on_24),
                                contentDescription = "切换",
                                //tint = Color(0xFFEFEEF6),
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (!flagCharge) {
            // 显示 LazyColumn 或 错误消息

            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
            ) {
                // 使用 items 而不是 forEach 来处理列表
                items(stationList.value ?: emptyList()) { station ->
                    StationInfoItem(station,navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }


        } else {
            //ToastUtil.showCustomToast(MyApp.getContext(),requestList.toString(),R.drawable.icon)
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
            ) {
                items(requestList.value ?: emptyList()) { request ->
                    RequestItem(request)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun RequestItem(request: UserRequest) {

    var showDialog by remember { mutableStateOf(false) }

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

            I3Item2(request)

    }
}
@Composable
fun StationInfoItem(infor: Station,navController:NavController) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(
                onClick = {
                    navController.navigate("target_screen/${infor.stationId}/${infor.storeName}")
                    //ToastUtil.showCustomToast(MyApp.getContext(),infor.stationId.toString())

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
            I3Item(infor)
        }
    }
}

@Composable
fun I3Item(stationInfo: Station) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "供货站名字: ${stationInfo.storeName}",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "交流方式: ${stationInfo.contactInfo}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "地址信息: ${stationInfo.addressDetails}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
        )
//        Text(
//            text = "数量: ${order.num}",
//            fontSize = 14.sp,
//            color = MaterialTheme.colorScheme.onSurface,
//            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
//        )
        Text(
            text = "状态: ${stationInfo.stationStatus}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
        )

    }
}


@Composable
fun I3Item2(userRequest: UserRequest) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "用户名: ${userRequest.username}",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "用户手机号: ${userRequest.phone}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
        Column {
            userRequest.requestProductInfoDTO.forEach { user ->
                Text(
                    text = "商品名称: ${user.productName}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
                )
                Text(
                    text = "需求数量: ${user.quantity}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

//        Text(
//            text = "数量: ${order.num}",
//            fontSize = 14.sp,
//            color = MaterialTheme.colorScheme.onSurface,
//            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
//        )


    }
}