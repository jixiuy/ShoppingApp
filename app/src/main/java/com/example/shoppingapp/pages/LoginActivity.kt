package com.example.shoppingapp.pages

import ToastUtil
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.MyApp
import com.example.shoppingapp.R
import com.example.shoppingapp.tools.ActivityManager
import com.example.shoppingapp.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        loginViewModel = (application as MyApp).loginViewModel
        ActivityManager.addActivity(this)
        setContent {

            CompositionLocalProvider(LocalLoginViewModel provides loginViewModel) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login"){
                    composable("login"){LoginScreen(navController)}
                    composable("register"){ RegisterPage()}
                }
            }

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.removeActivity(this)
    }
    private lateinit var loginViewModel: LoginViewModel
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginScreen(navController: NavHostController) {
        val context = LocalContext.current
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()



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
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "欢迎使用",
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
                    Image(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(45.dp)
                            .padding(top = 1.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 账号输入框
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)

                    ,
                ) {
                    OutlinedTextField(
                        value = username.value,
                        onValueChange = { username.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        placeholder = { Text(text = "请输入您的账号") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF515D87),  // 获得焦点时的边框颜色
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    )
                }

                // 密码输入框
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                    ,

                ) {
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        placeholder = { Text(text = "请输入您的密码") },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF515D87),  // 获得焦点时的边框颜色
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    )
                }

                // 登录按钮
                Button(
                    onClick = {
                        coroutineScope.launch {
                            loginViewModel.login(username.value, password.value)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(56.dp)

                    , // 设置按钮高度
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEFEEF6),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "立即登录",
                        color = Color(0xFF515D87),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // 忘记密码和注册登录按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { /* 忘记密码操作 */ }) {
                        Text(text = "忘记密码", color = Color(0xFF717273))
                    }
                    TextButton(onClick = {
                        navController.navigate("register")
                    }) {
                        Text(text = "注册登录", color = Color(0xFF717273))
                    }
                }


            }
        }
        val loginResponse by loginViewModel.loginResponse.collectAsState(initial = null)

//        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()


        // 当 loginResponse 更新时进行状态更新
        loginResponse?.let { response ->
            if (response.code == 200 && MyApp.count ==0) {
                GlobalToken.token = response.data?.token
//                editor.putString("token", GlobalToken.token)  // 保存 token
//                editor.apply()  // 提交更改
                ToastUtil.showCustomToast(context,"登录成功",R.drawable.icon)
                GlobalToken.role = response.data?.role
                (context as? Activity)?.finish()
                MyApp.count = MyApp.count!! + 1

            } else if(MyApp.count!! >=1){
                ToastUtil.showCustomToast(context,"已经登录过了，不允许重复登录",R.drawable.icon)
                (context as? Activity)?.finish()
            }else{
                ToastUtil.showCustomToast(context,"登录失败",R.drawable.icon)
            }
        }



    }


}
