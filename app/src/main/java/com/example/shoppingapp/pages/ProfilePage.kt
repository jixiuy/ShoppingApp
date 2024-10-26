import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.R
import com.example.shoppingapp.pages.HistoryAccount
import com.example.shoppingapp.pages.LocalLoginViewModel
import com.example.shoppingapp.pages.LoginActivity
import com.example.shoppingapp.viewmodel.LoginViewModel
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfilePage() {

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {}) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Header(paddingValues = it)

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Card1()
                Spacer(modifier = Modifier.height(16.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Logout()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

}

@Composable
fun Header(paddingValues: PaddingValues) {
    val context = LocalContext.current

    val loginResponse by LocalLoginViewModel.current.loginResponse.collectAsState(initial = null)

    var username: String? = null
    loginResponse?.let { response ->
        Log.d("Header1111", "response changed: $response")
        if (response.code == 200) {
            username = response.data?.username
        } else {
            username = "请先登录"
        }
    }

    Box(
        modifier = Modifier,
    ) {

        Card(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEEF6))

        ) {
            Box(modifier = Modifier
                .clickable {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
                .padding(16.dp)

            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        painter = painterResource(id = R.drawable.wechatjpg),
                        contentDescription = null
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            text = username ?: "请先登录", fontSize = 20.sp, color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Card1() {
    ElevatedCard {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
        ) {
            HistoryBill()
            ModifyProfile()
            BecomeDriver()
            BecomeSupplier()
        }
    }
}

@Composable
fun HistoryBill() {
    val context = LocalContext.current
    Item(onClick = {
        val intent = Intent(context, HistoryAccount::class.java)
        context.startActivity(intent)
    }, icon = {
        Icon(
            painter = painterResource(id = R.drawable.baseline_history_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }, text = {
        Text(text = "历史订单")
    })
}

@Composable
fun ModifyProfile() {
    val context = LocalContext.current
    Item(onClick = {

    }, icon = {
        Icon(
            Icons.Filled.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary
        )
    }, text = {
        Text(text = "完善个人信息")
    })
}

@Composable
fun BecomeDriver(driverViewModel: DriverViewModel = DriverViewModel()) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    // 观察注册状态
    val registerStatus by driverViewModel.registerStatus.observeAsState(initial = false)

    // 点击事件：司机角色检查
    Item(onClick = {
        if (registerStatus) {
            ToastUtil.showCustomToast(context, "已经是司机", R.drawable.icon)
        } else {
            showDialog = true
        }
    }, icon = {
        Icon(
            painter = painterResource(id = R.drawable.baseline_directions_car_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }, text = { Text(text = "成为司机") })

    // 显示对话框输入信息
    if (showDialog) {
        TextInputDialog(onDismiss = { showDialog = false }, onConfirm = { license, location ->

            coroutineScope.launch {
                if (GlobalToken.token == null) {
                    ToastUtil.showCustomToast(context, "请先登录", R.drawable.icon)
                } else {
                    driverViewModel.registerDriver(
                        context, license, location, GlobalToken.token.toString()
                    )
                }

            }
            showDialog = false
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputDialog(
    onDismiss: () -> Unit, onConfirm: (String, String) -> Unit // 用于返回两个文本输入的内容
) {
    var firstText by remember { mutableStateOf("") }
    var secondText by remember { mutableStateOf("") }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("输入汽车牌照和位置信息") }, text = {
        Column {
            OutlinedTextField(
                value = firstText,
                onValueChange = { firstText = it },
                label = { Text("骑车牌照") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = secondText,
                onValueChange = { secondText = it },
                label = { Text("位置信息") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }, confirmButton = {
        TextButton(onClick = {
            onConfirm(firstText, secondText)
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
fun BecomeSupplier() {
    val context = LocalContext.current
    val stationViewModel: StationViewModel = StationViewModel()
    var showDialog by remember {
        mutableStateOf(false)
    }
    val registerStatus by stationViewModel.registerStatus.observeAsState(initial = false)
    Item(onClick = {
        if (registerStatus) {
            ToastUtil.showCustomToast(
                context = context,
                message = "已经是供货人员",
                iconResId = R.drawable.icon // 替换为你的图标资源
            )
        } else showDialog = true
    }, icon = {
        Icon(
            painter = painterResource(id = R.drawable.baseline_local_shipping_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }, text = {
        Text(text = "成为供货人员")
    })

    if (showDialog) RegisterStationDialog(onDismiss = { showDialog = false })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStationDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var storeName by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") }
    var addressDetails by remember { mutableStateOf("") }
    var stationType by remember { mutableStateOf("") }
    val stationViewModel: StationViewModel = viewModel()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("注册供货站") },
        text = {
            Column {
                OutlinedTextField(
                    value = storeName,
                    onValueChange = { storeName = it },
                    label = { Text("商店名称") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = contactInfo,
                    onValueChange = { contactInfo = it },
                    label = { Text("联系信息") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = addressDetails,
                    onValueChange = { addressDetails = it },
                    label = { Text("地址详情") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = stationType,
                    onValueChange = { stationType = it },
                    label = { Text("站点类型") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val token = GlobalToken.token // 替换为实际的token
                if (token != null) {
                    stationViewModel.registerStation(
                        context,
                        storeName,
                        contactInfo,
                        addressDetails,
                        stationType,
                        token
                    )
                } else {
                    ToastUtil.showCustomToast(context, "请先登录", R.drawable.icon)
                }
                onDismiss() // 关闭对话框
            }) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
fun Item(
    onClick: () -> Unit,
    icon: @Composable RowScope.() -> Unit,
    text: @Composable RowScope.() -> Unit
) {
    Row(modifier = Modifier
        .padding(vertical = 4.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .clickable {
            onClick()
        }
        .padding(start = 14.dp, end = 14.dp, top = 14.dp, bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically) {
        icon()
        Spacer(modifier = Modifier.width(16.dp))
        text()
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            Icons.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun Logout() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    ElevatedButton(
        onClick = {
            editor.remove("token")  // 移除 token
            editor.apply()
            (context as? Activity)?.finishAffinity()
        },
        Modifier
            .padding(0.dp)
            .fillMaxWidth()
    ) {
        Text(text = "退出应用")
    }
}

