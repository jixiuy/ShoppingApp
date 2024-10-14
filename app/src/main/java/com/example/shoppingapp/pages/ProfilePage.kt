import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppingapp.R
import com.example.shoppingapp.models.LoginResponse
import com.example.shoppingapp.viewmodel.LoginViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfilePage(loginViewModel: LoginViewModel) {
    val loginResponse by loginViewModel.loginResponse

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Header(paddingValues = it,loginResponse = loginResponse)
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Card1(loginResponse = loginResponse)
                Spacer(modifier = Modifier.height(16.dp))
                //Card2()
                Spacer(modifier = Modifier.height(16.dp))
                Logout()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

}

@Composable
fun Header(paddingValues: PaddingValues, loginResponse: LoginResponse?) {
    val username = loginResponse?.data?.username ?: "用户名"

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

                }
                .padding(16.dp)

            )
            {
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
                            text =username,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Card1(loginResponse: LoginResponse?) {
    ElevatedCard {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
        ) {
            HistoryBill()
            ModifyProfile()
            loginResponse?.data?.let { BecomeDriver(it.role) }
            loginResponse?.data?.let { BecomeSupplier(it.role) }
        }
    }
}

@Composable
fun HistoryBill() {
    val context = LocalContext.current
    Item(
        onClick = {

        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_history_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Text(text = "历史订单")
        }
    )
}

@Composable
fun ModifyProfile() {
    val context = LocalContext.current
    Item(
        onClick = {

        },
        icon = {
            Icon(
                Icons.Filled.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Text(text = "完善个人信息")
        }
    )
}

@Composable
fun BecomeDriver(role: Int) {
    val context = LocalContext.current
    Item(
        onClick = {
            if(role==2||role==4){
                ToastUtil.showCustomToast(
                    context = context,
                    message = "已经是司机",
                    iconResId = R.drawable.icon // 替换为你的图标资源
                )
            }else{

            }
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_directions_car_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Text(text = "成为司机")
        }
    )
}

@Composable
fun BecomeSupplier(role: Int) {
    val context = LocalContext.current
    Item(
        onClick = {
            if(role==3||role==4){
                ToastUtil.showCustomToast(
                    context = context,
                    message = "已经是供货人员",
                    iconResId = R.drawable.icon // 替换为你的图标资源
                )
            }else{

            }
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_local_shipping_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Text(text = "成为供货人员")
        }
    )
}

@Composable
fun Item(
    onClick: () -> Unit,
    icon: @Composable RowScope.() -> Unit,
    text: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
            .padding(start = 14.dp, end = 14.dp, top = 14.dp, bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
    ElevatedButton(
        onClick = {
            (context as? Activity)?.finishAffinity()
        },
        Modifier
            .padding(0.dp)
            .fillMaxWidth()
    ) {
        Text(text =  "退出应用")
    }
}