package com.nauh.waterqualitymonitor.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.R
import com.nauh.waterqualitymonitor.data.network.RetrofitClient
import com.nauh.waterqualitymonitor.ui.components.TopBar
import com.nauh.waterqualitymonitor.ui.theme.Shapes
import com.nauh.waterqualitymonitor.ui.theme.Typography
import com.nauh.waterqualitymonitor.viewmodels.DashboardViewModel
import com.nauh.waterqualitymonitor.viewmodels.DashboardViewModelFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun Dashboard(navController: NavController, username: String) {
    // Thu thập dữ liệu từ ViewModel
    val context = LocalContext.current
    val viewModel: DashboardViewModel = viewModel(factory = DashboardViewModelFactory(context))

    val dashboardData by viewModel.dashboardData.collectAsState()

    // Lấy ngưỡng từ tài nguyên
    val turbidityThreshold = stringResource(id = R.string.threshold_tds).toDouble()
    val temperatureThreshold = stringResource(id = R.string.threshold_temperature).toDouble()
    val warningColor = colorResource(id = R.color.warning)
    val water = colorResource(id = R.color.water)
    val relay = colorResource(id = R.color.green)
    val gray = colorResource(id = R.color.gray)

    // Giao diện màn hình Dashboard
    Scaffold(
        topBar = {
            TopBar(pageTitle = "Tổng quan", onAccountClick = { navController.navigate("login") }, username = username)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hiển thị thẻ cho độ đục
                val turbidityValue = dashboardData.turbidity.split(" ")[0].toDoubleOrNull() ?: 0.0
                val turbidityTextColor = if (turbidityValue > turbidityThreshold) Color.White else Color.Black
                DashboardCardView(
                    title = "Độ Đục",
                    value = dashboardData.turbidity,
                    route = "dashboard_detail/turbidity",
                    onClick = { navController.navigate("dashboard_detail/turbidity") },
                    color = if (turbidityValue > turbidityThreshold) warningColor else gray,
                    textColor = turbidityTextColor
                )

                // Hiển thị thẻ cho nhiệt độ
                val temperatureValue = dashboardData.temperature.split("°")[0].toDoubleOrNull() ?: 0.0
                val temperatureTextColor = if (temperatureValue > temperatureThreshold) Color.White else Color.Black
                DashboardCardView(
                    title = "Nhiệt Độ",
                    value = dashboardData.temperature,
                    route = "dashboard_detail/temperature",
                    onClick = { navController.navigate("dashboard_detail/temperature") },
                    color = if (temperatureValue > temperatureThreshold) warningColor else gray,
                    textColor = temperatureTextColor
                )

                // Hiển thị thẻ cho lưu lượng nước
                val flowRateValue = dashboardData.flowRate.toDoubleOrNull() ?: 0.0
                DashboardCardView(
                    title = "Lưu Lượng Nước",
                    value = dashboardData.flowRate,
                    route = "dashboard_detail/flow_rate",
                    onClick = { navController.navigate("dashboard_detail/flow_rate") },
                    color = water,
                    textColor = Color.White // Mặc định là màu đen
                )

                // Hiển thị thẻ cho trạng thái relay
                DashboardCardView(
                    title = "Trạng Thái Relay",
                    value = dashboardData.relayStatus,
                    route = "dashboard_detail/relay",
                    onClick = { navController.navigate("dashboard_detail/relay") },
                    color = if (dashboardData.relayStatus == "Tắt") warningColor else relay,
                    textColor = Color.White // Giữ màu chữ là trắng
                )

                // Hiển thị các nút điều khiển nếu là admin
                if (username == stringResource(id = R.string.admin)) {
                    ControlWaterButtons()
                }

                // Log dữ liệu
                Log.d("Dashboard", "Turbidity: ${dashboardData.turbidity}")
                Log.d("Dashboard", "Temperature: ${dashboardData.temperature}")
                Log.d("Dashboard", "Flow Rate: ${dashboardData.flowRate}")
                Log.d("Dashboard", "Relay: ${dashboardData.relayStatus}")
            }
        }
    )
}

@Composable
fun ControlWaterButtons() {
    // Mặc định nút "On" được chọn
    val (selectedButton, setSelectedButton) = androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf("On")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center // Đặt Card vào giữa Box
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Lề ngang của Card
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp), // Khoảng cách trên dưới của hàng nút
                horizontalArrangement = Arrangement.SpaceEvenly, // Các nút cách đều nhau
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nút "On"
                ControlButton(
                    text = "On",
                    backgroundColor = if (selectedButton == "On") MaterialTheme.colorScheme.primary else Color.Gray,
                    onClick = {
                        setSelectedButton("On")
                        sendRelayCommand("on")
                    }
                )

                // Nút "Off"
                ControlButton(
                    text = "Off",
                    backgroundColor = if (selectedButton == "Off") MaterialTheme.colorScheme.secondary else Color.Gray,
                    onClick = {
                        setSelectedButton("Off")
                        sendRelayCommand("off")
//                        Log.d("RelayCommand", "Sending relay command: off")
                    }
                )

                // Nút "Auto"
                ControlButton(
                    text = "Auto",
                    backgroundColor = if (selectedButton == "Auto") MaterialTheme.colorScheme.tertiary else Color.Gray,
                    onClick = {
                        setSelectedButton("Auto")
                        sendRelayCommand("auto")
                    }
                )
            }
        }
    }
}



//@OptIn(DelicateCoroutinesApi::class)
//fun sendRelayCommand(command: String) {
//    val json = mapOf("relay" to command)
//
//    // Log thông báo gửi lệnh
//    Log.d("RelayCommand", "Sending relay command: $command")
//
//    // Gọi RetrofitClient để gửi lệnh tới backend
//    GlobalScope.launch(Dispatchers.IO) {
//        RetrofitClient.sendRelayCommand(json)
//    }
//}

@OptIn(DelicateCoroutinesApi::class)
fun sendRelayCommand(command: String) {
    // Tạo chuỗi JSON stringified
    val jsonString = JSONObject(mapOf("relay" to command)).toString()

    // Log thông báo gửi lệnh
    Log.d("RelayCommand", "Sending relay command: $jsonString")

    // Gọi RetrofitClient để gửi lệnh tới backend
    GlobalScope.launch(Dispatchers.IO) { // Sử dụng Coroutine đúng cách
        try {
            RetrofitClient.sendRelayCommand(jsonString)
        } catch (e: Exception) {
            Log.e("RelayCommand", "Error sending relay command: ${e.message}")
        }
    }
}
//val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
//
//fun sendRelayCommand(command: String) {
//    val jsonString = JSONObject(mapOf("relay" to command)).toString()
//
//    lifecycleScope.launch(Dispatchers.IO) {
//        try {
//            RetrofitClient.sendRelayCommand(jsonString)
//            Log.d("RelayCommand", "Command sent successfully: $command")
//        } catch (e: Exception) {
//            Log.e("RelayCommand", "Failed to send command: ${e.message}")
//        }
//    }
//}




@Composable
fun ControlButton(
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 100.dp, height = 40.dp) // Kích thước nút cố định
            .clickable(onClick = onClick)
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.small // Bo góc nhẹ
            ),
        contentAlignment = Alignment.Center // Căn giữa chữ trong nút
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
    }
}




@Composable
fun DashboardCardView(
    title: String,
    value: String,
    route: String,
    onClick: () -> Unit,
    color: Color,
    textColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = color) // Thay đổi màu nền của card
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = Typography.titleMedium.copy(color = textColor) // Sử dụng textColor
            )
            Text(
                text = value,
                style = Typography.bodyLarge.copy(color = textColor) // Sử dụng textColor
            )
        }
    }
}



