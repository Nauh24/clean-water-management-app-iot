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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.R
import com.nauh.waterqualitymonitor.ui.components.TopBar
import com.nauh.waterqualitymonitor.ui.theme.Shapes
import com.nauh.waterqualitymonitor.ui.theme.Typography
import com.nauh.waterqualitymonitor.viewmodels.DashboardViewModel
import com.nauh.waterqualitymonitor.viewmodels.DashboardViewModelFactory

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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp), // Tạo khoảng cách giữa khung chứa và thẻ phía trên
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Khung chứa các nút
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Lề hai bên của khung chứa
            elevation = CardDefaults.cardElevation(4.dp), // Hiệu ứng nổi
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp), // Padding bên trong khung
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Hàng chứa nút "On" và "Off"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ControlButton(
                        text = "On",
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        onClick = { /* Handle On Action */ }
                    )
                    ControlButton(
                        text = "Off",
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        onClick = { /* Handle Off Action */ }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp)) // Khoảng cách giữa các hàng

                // Nút "Auto" nằm chính giữa
                ControlButton(
                    text = "Auto",
                    backgroundColor = MaterialTheme.colorScheme.tertiary,
                    onClick = { /* Handle Auto Action */ },
                    modifier = Modifier.fillMaxWidth(0.5f) // Rộng 50% khung chứa
                )
            }
        }
    }
}

@Composable
fun ControlButton(
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 80.dp, height = 36.dp) // Kích thước nhỏ hơn
            .clickable(onClick = onClick)
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.small // Bo góc nhẹ
            ),
        contentAlignment = Alignment.Center // Căn chữ vào giữa nút
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White, // Màu chữ trắng
                fontWeight = FontWeight.Bold // Chữ đậm
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



