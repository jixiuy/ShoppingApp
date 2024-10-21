package com.example.shoppingapp.pages

import DriverViewModel
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.models.DriverBean

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DriverGoodsPage() {
    val driverViewModel: DriverViewModel = viewModel()

    GlobalToken.token?.let { driverViewModel.getCarGoodsInfo(it) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("车辆商品信息", fontSize = 20.sp) },
            )
        }
    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
        ) {
            items(driverViewModel.carGoodsInfo.value?.data ?: emptyList()) { infor ->
                CarItem(infor)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }


}


@Composable
fun CarItem(infor: DriverBean.Data) {
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