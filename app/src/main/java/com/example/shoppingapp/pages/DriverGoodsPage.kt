package com.example.shoppingapp.pages

import DriverViewModel
import StationViewModel
import ToastUtil
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.MyApp
import com.example.shoppingapp.R
import com.example.shoppingapp.models.DriverBean
import com.example.shoppingapp.models.StationBean
import com.example.shoppingapp.viewmodel.ProductViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DriverGoodsPage() {
    val navController = rememberNavController()

    // 定义局部导航图
    NavHost(navController = navController, startDestination = "DriverGoodsPageMain") {
        composable("DriverGoodsPageMain") {GoosPage(navController) }
        composable("AddCarGoodsPage") { AddCarGoodsPage(navController) }
        composable("AddStationGoodsPage") { AddStationGoodsPage(navController) }
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun GoosPage(navController: NavHostController) {
    val driverViewModel: DriverViewModel = viewModel()
    val stationViewModel: StationViewModel = viewModel()

    // 观察 Station 数据的变化
    val stationGoodsInfo: StationBean? by stationViewModel.stationGoodsInfo.observeAsState()
    val carGoodsInfo: DriverBean? by driverViewModel.carGoodsInfo.observeAsState()


    if (GlobalToken.role == 2 || GlobalToken.role == 4) {
        GlobalToken.token?.let { driverViewModel.getCarGoodsInfo(it) }
    }
    if (GlobalToken.role == 3 || GlobalToken.role == 4) {
        GlobalToken.token?.let { stationViewModel.getStationGoodsInfo(it) }
    }
    val isLoading by driverViewModel.isLoading.observeAsState(false)

    var flagCharge by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (!flagCharge) Text("车辆商品信息", fontSize = 20.sp)
                    else Text("供货站商品信息", fontSize = 20.sp)
                },
                actions = {
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (!flagCharge) {
                    navController.navigate("AddCarGoodsPage")
                } else {
                    navController.navigate("AddStationGoodsPage")
                }
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "添加商品")
            }
        }
    ) { paddingValues ->

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isLoading),
            onRefresh = {
                // 刷新数据
                if (!flagCharge) {
                    GlobalToken.token?.let { driverViewModel.getCarGoodsInfo(it) }
                } else {
                    GlobalToken.token?.let { stationViewModel.getStationGoodsInfo(it) }
                }
            }
        ) {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
            ) {
                if (!flagCharge) {
                    items(carGoodsInfo?.data?: emptyList()) { infor ->
                        Spacer(modifier = Modifier.height(8.dp))
                        CarItem(infor = infor)
                    }
                } else {

                    items(stationGoodsInfo?.data?: emptyList()) { infor ->
                        Spacer(modifier = Modifier.height(8.dp))
                        StationItem(infor = infor)
                    }
                }
            }
        }
    }
}


@Composable
fun CarItem(infor: DriverBean.Data) {
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val viewModel: DriverViewModel = viewModel()
    if(showDialog){
        TextInputDialog2(
            onDismiss = { showDialog = false },
            onConfirm = { action, quantity ->
                // 处理选择的操作和输入的数量
                when (action) {
                    "增加" -> {
                        GlobalToken.token?.let {
                            viewModel.increaseStock(infor.id,Integer.valueOf(quantity),
                                it
                            )
                        }
                    }
                    "减少" -> {
                        GlobalToken.token?.let {
                            viewModel.decreaseStock(infor.id,Integer.valueOf(quantity),
                                it
                            )
                        }
                    }
                    "修改" -> {
                        GlobalToken.token?.let {
                            viewModel.changeStock(infor.id,Integer.valueOf(quantity),
                                it
                            )
                        }
                    }
                }
            },
            onDelete = {
                coroutineScope.launch {
                    GlobalToken.token?.let { viewModel.deleteCarGoodsInfo(infor.id, it) }
                }
            }
        )
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

            IItem(infor)
        }
    }


}

@Composable
fun IItem(order: DriverBean.Data) {
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
            text = "类型: ${order.type}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
        )
        Text(
            text = "数量: ${order.num}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
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
fun StationItem(infor: StationBean.Data) {
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val viewModel: StationViewModel = viewModel()

    if(showDialog){
        TextInputDialog2(
            onDismiss = { showDialog = false },
            onConfirm = { action, quantity ->
                // 处理选择的操作和输入的数量
                when (action) {
                    "增加" -> {
                        GlobalToken.token?.let {
                            viewModel.incrementProduct(infor.id,Integer.valueOf(quantity),
                                it
                            )
                        }
                    }
                    "减少" -> {
                        GlobalToken.token?.let {
                            viewModel.decrementProduct(infor.id,Integer.valueOf(quantity),
                                it
                            )
                        }
                    }
                    "修改" -> {
                        GlobalToken.token?.let {
                            viewModel.modifyProduct(infor.id,Integer.valueOf(quantity),
                                it
                            )
                        }
                    }
                }
            },
            onDelete = {
                coroutineScope.launch {
                    GlobalToken.token?.let { viewModel.deleteProductInfo(infor.id, it) }
                }
            }
        )
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
                painter = rememberAsyncImagePainter(model = infor.imgUrl),
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(80.dp)
                    .padding(start = 5.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            I2Item(infor)
        }
    }


}

@Composable
fun I2Item(order: StationBean.Data) {
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
            text = "类型: ${order.type}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
        )
        Text(
            text = "数量: ${order.num}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
        )
        Text(
            text = "详细信息: ${order.descInfo}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputDialog2(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit, // 用于返回选择的操作和输入的数量
    onDelete: () ->Unit
) {
    var secondText by remember { mutableStateOf("") }
    var selectedAction by remember { mutableStateOf("增加") }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("选择操作") },
            confirmButton = {
            },
            text = {
                Row {
                    listOf("增加", "减少", "修改").forEach { action ->
                        Button(onClick = {
                            selectedAction = action
                            showDialog = false
                        }) {
                            Text(action)
                        }
                        Spacer(modifier = Modifier.padding(3.dp))
                    }
                }
            }
        )

    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("修改商品库存") },
        text = {
            Column {

                OutlinedTextField(
                    value = selectedAction,
                    onValueChange = { /* 不需要实现此处的变化 */ },
                    label = { Text("选择操作") },
                    trailingIcon = {
                        IconButton(onClick = { showDialog = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "选择操作")
                        }
                    }
                )
                // 输入数量的文本框
                OutlinedTextField(
                    value = secondText,
                    onValueChange = { secondText = it },
                    label = { Text("数量") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // 限制输入为数字
                )
            }
        },
        confirmButton = {
            Row {
                TextButton(onClick = {
                    onConfirm(selectedAction, secondText)
                    onDismiss()
                }) {
                    Text("确认")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = {
                    selectedAction = "删除"
                    onDelete()
                    onDismiss()
                }) {
                    Text("删除")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        },
        properties = DialogProperties(dismissOnClickOutside = false)
    )
}
