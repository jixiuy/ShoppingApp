package com.example.shoppingapp.pages
import androidx.lifecycle.viewModelScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.models.ProductResponse
import com.example.shoppingapp.viewmodel.BuyGoodsViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun AddCarGoodsPage(navController: NavController) {
    GoodsPage(title = "添加车辆商品",navController,1)
}

@Composable
fun AddStationGoodsPage(navController: NavController) {
    GoodsPage(title = "添加供货站商品",navController,2)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodsPage(title: String, navController: NavController, type: Int) {
    var searchText by remember { mutableStateOf("") }

    val buyGoodsViewModel: BuyGoodsViewModel = viewModel()
    val products by buyGoodsViewModel.products.observeAsState(emptyList())
    val coroutine = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        // 顶部标题
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
             // 设置左右间距
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "返回")
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center // 设置内容居中
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = 20.sp
                )
            }
        }

        // 输入框和确认按钮的行布局
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("查询商品信息") }, // 使用 placeholder 代替 label
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(12.dp), // 设置圆角
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFFF0F0F0), // 设置输入框背景颜色
                    placeholderColor = Color.Gray, // 设置placeholder文字颜色
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Button(onClick = {
                coroutine.launch {
                    GlobalToken.token?.let { buyGoodsViewModel.fetchProducts(searchText, it) }
                }
            }) {
                Text("搜索")
            }
        }

        // 使用 LazyColumn 展示搜索结果
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            items(products) { product ->
                MyItem(product,type,buyGoodsViewModel)
            }
        }
    }
}

@Composable
fun MyItem(product: ProductResponse.Product, type: Int, buyGoodsViewModel: BuyGoodsViewModel) {
    var isShow by remember {
        mutableStateOf(false)
    }
    GoodsDialog(isShow, onDismiss = {
        isShow=false
    }, onConfirm = {
        inputData(product.id,type,buyGoodsViewModel)
    })
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(
                onClick = {
                    isShow = true
                },
                indication = rememberRipple(color = MaterialTheme.colorScheme.primary), // 添加水波纹效果
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
                    data = product.imgUrl,
                ),
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(80.dp)
                    .padding(start = 5.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            AddGoodsMyText(product)
        }
    }
}


fun inputData(id: Int, type: Int, buyGoodsViewModel: BuyGoodsViewModel) {

    when(type){
        1->{
                GlobalToken.token?.let { buyGoodsViewModel.addVehicleProduct(id, it) }
        }
        2->{
            GlobalToken.token?.let { buyGoodsViewModel.addReplenishmentStationProduct(id, it) }
        }
    }
}
@Composable
fun GoodsDialog(show: Boolean,onDismiss:()->Unit,onConfirm: ()->Unit) {
    if(show){
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("确认添加商品") },
            text = { Text("您确定要添加此商品吗？") },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                    onDismiss()
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("取消")
                }
            }
        )
    }
}


@Composable
fun AddGoodsMyText(product: ProductResponse.Product) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "商品名称: ${product.name}",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "价格: $${product.price}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "商品类型: ${product.descInfo}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
        )
        Text(
            text = "详细信息: ${product.descInfo}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
        )

    }
}