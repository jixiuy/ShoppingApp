package com.example.shoppingapp.pages

import DriverViewModel
import StationViewModel
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.models.StationProductResponse
import com.example.shoppingapp.models.VehicleRequest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationGoodsPage( stationId:Int, stationName:String,    navController: NavController) {

    // 观察 ViewModel 中的产品列表
    val stationViewModel: StationViewModel = viewModel()
    val driverViewModel: DriverViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }

    // 观察 ViewModel 中的产品列表
    val goodsList by stationViewModel.productList.collectAsState(emptyList())
    val selectedProducts = remember { mutableStateListOf<StationProductResponse.Product>() }
    var showDialog by remember { mutableStateOf(false) }
    val quantities = remember { mutableStateMapOf<Int, String>() } // 保存每个产品的数量

    // 模拟网络请求
    LaunchedEffect(stationId) {
        coroutineScope.launch {
            GlobalToken.token?.let { stationViewModel.loadProducts(stationId, it) }
            isLoading = false // 设置加载完成
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("供货站：${stationName}货物信息", fontSize = 20.sp)
                }, navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedProducts.isNotEmpty()) {
                        showDialog = true // 打开对话框
                    }
                },
                content = {
                    Icon(Icons.Default.Check, contentDescription = "确认选择")
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(goodsList) { product ->
                        I3Item2(product, selectedProducts)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = {
                Column {
                    selectedProducts.forEach { product ->
                        Text(text = "商品: ${product.name}")
                        TextField(
                            value = quantities[product.id] ?: "", // 使用状态中的数量值
                            onValueChange = { quantities[product.id] = it },
                            label = { Text("请输入数量") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.padding(vertical = 3.dp))
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // 在这里处理网络请求
                        val requests = selectedProducts.mapNotNull { product ->
                            quantities[product.id]?.takeIf { it.isNotEmpty() }?.let { quantity ->
                                VehicleRequest(id = product.id, quantity = quantity.toInt())
                            }
                        }

                        GlobalToken.token?.let {
                            driverViewModel.loadProducts(stationId, it, requests)
                        }
                        // 清空选择状态
                        selectedProducts.clear()
                        showDialog = false
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun I3Item2(
    product: StationProductResponse.Product,
    selectedProducts: MutableList<StationProductResponse.Product>
) {

    val isSelected = selectedProducts.contains(product)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(
                onClick = {
                    if (isSelected) {
                        selectedProducts.remove(product)
                    } else {
                        selectedProducts.add(product)
                    }
                },
                indication = rememberRipple(), // 添加水波纹效果
                interactionSource = remember { MutableInteractionSource() }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                // 选择圆点
                RadioButton(
                    selected = isSelected,
                    onClick = {
                        if (isSelected) {
                            selectedProducts.remove(product)
                        } else {
                            selectedProducts.add(product)
                        }
                    }
                )
                Image(
                    painter = rememberImagePainter(data = product.imgUrl),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(start = 10.dp)
                )
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "货品名: ${product.name}",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "货品价格: ${product.price}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "货品类型: ${product.type}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "货品数量: ${product.num}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "货品描述: ${product.descInfo}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

        }
    }

}