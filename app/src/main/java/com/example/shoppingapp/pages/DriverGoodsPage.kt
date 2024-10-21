package com.example.shoppingapp.pages

import DriverViewModel
import StationViewModel
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.R
import com.example.shoppingapp.models.DriverBean
import com.example.shoppingapp.models.StationBean

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DriverGoodsPage() {
    val driverViewModel: DriverViewModel = viewModel()
    val stationViewModel: StationViewModel = viewModel()
    if (GlobalToken.role == 2 || GlobalToken.role == 4) {
        GlobalToken.token?.let { driverViewModel.getCarGoodsInfo(it) }
    }
    if (GlobalToken.role == 3 || GlobalToken.role == 4) {
        GlobalToken.token?.let { stationViewModel.getStationGoodsInfo(it) }
    }


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
        }
    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
        ) {
            if (!flagCharge) {
                items(driverViewModel.carGoodsInfo.value?.data ?: emptyList()) { infor ->
                    Spacer(modifier = Modifier.height(8.dp))
                    CarItem(infor = infor)
                }
            } else {
                items(stationViewModel.stationGoodsInfo.value?.data ?: emptyList()) { infor ->
                    Spacer(modifier = Modifier.height(8.dp))
                    StationItem(infor = infor)
                }
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


@Composable
fun StationItem(infor: StationBean.Data) {
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
//        Text(
//            text = "数量: ${order.num}",
//            fontSize = 14.sp,
//            color = MaterialTheme.colorScheme.onSurface,
//            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
//        )
        Text(
            text = "详细信息: ${order.descInfo}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)
        )

    }
}