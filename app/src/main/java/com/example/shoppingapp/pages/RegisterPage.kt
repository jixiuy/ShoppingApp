package com.example.shoppingapp.pages

import ToastUtil
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.MyApp
import com.example.shoppingapp.R
import com.example.shoppingapp.models.User
import com.example.shoppingapp.viewmodel.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(registerViewModel: RegisterViewModel = viewModel()) {
    var nickName by remember { mutableStateOf("") }
    var realName by remember { mutableStateOf("") }
    var userMobile by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val coroutine = rememberCoroutineScope()

    val context = LocalContext.current
    val registerResponse = registerViewModel.registerStatus.observeAsState()
    registerResponse.let {
        if(it.value==true)(context as Activity).finish()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 45.dp, start = 30.dp, end = 30.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 欢迎文本和图标
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "注册",
                    color = Color(0xFF585757),
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ShoppingApp",
                    color = Color(0xFF515D87),
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "账号",
                    color = Color(0xFF585757),
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            InputField(
                value = nickName,
                onValueChange = { nickName = it },
                placeholderText = "请输入您的用户名"
            )

            InputField(
                value = realName,
                onValueChange = { realName = it },
                placeholderText = "请输入您的真实名字"
            )

            InputField(
                value = userMobile,
                onValueChange = { userMobile = it },
                placeholderText = "请输入您的手机号"
            )

            InputField(
                value = sex,
                onValueChange = { sex = it },
                placeholderText = "请输入您的性别"
            )

            InputField(
                value = password,
                onValueChange = { password = it },
                placeholderText = "请输入密码"
            )

            InputField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholderText = "再次输入密码"
            )

            // 登录按钮
            Button(
                onClick = {
                    if (password != confirmPassword) {
                        ToastUtil.showCustomToast(MyApp.getContext(), "密码不匹配", R.drawable.icon)
                    } else {
                        coroutine.launch {
                            // 在后台执行注册请求
                            val user = User(nickName, realName, userMobile, sex, password)
                            registerViewModel.register(user)
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
                    text = "注册",
                    color = Color(0xFF515D87),
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = Modifier.height(3.dp))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            placeholder = { Text(text = placeholderText) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF515D87),  // 获得焦点时的边框颜色
            ),
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = Color.Black
            )
        )
    }
}
