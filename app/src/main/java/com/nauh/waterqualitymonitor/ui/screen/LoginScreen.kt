package com.nauh.waterqualitymonitor.ui.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.R
import com.nauh.waterqualitymonitor.ui.theme.WaterQualityMonitorTheme

@Composable
fun LoginScreen(navController: NavController, onLoginClick: (String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Đăng nhập", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(24.dp))

        // Trường nhập tài khoản
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Tài khoản") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Trường nhập mật khẩu
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nút đăng nhập
        Button(
            onClick = {
                // Kiểm tra tài khoản và mật khẩu
                if (isValidLogin(context, username, password)) {
                    onLoginClick(username) // Gọi hàm callback và truyền tên tài khoản
                    navController.navigate("dashboard") // Điều hướng về màn hình chính
                } else {
                    // Thông báo lỗi nếu đăng nhập sai
                    Toast.makeText(context, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Đăng nhập")
        }
    }
}

// Kiểm tra tài khoản và mật khẩu có đúng không
fun isValidLogin(context: Context, username: String, password: String): Boolean {
    // Lấy tài khoản và mật khẩu từ strings.xml
    val correctUser = context.getString(R.string.user)
    val correctPassword = context.getString(R.string.user_password)
    val correctAdmin = context.getString(R.string.admin)
    val correctAdminPassword = context.getString(R.string.admin_password)

    // In log để kiểm tra giá trị người dùng nhập
    Log.d("LoginDebug", "Entered username: $username, password: $password")
    Log.d("LoginDebug", "Correct user: $correctUser, password: $correctPassword")
    Log.d("LoginDebug", "Correct admin: $correctAdmin, password: $correctAdminPassword")

    // Kiểm tra xem tài khoản và mật khẩu có trùng không
    return (username == correctUser && password == correctPassword) ||
            (username == correctAdmin && password == correctAdminPassword)
}
