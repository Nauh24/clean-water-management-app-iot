package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.data.model.StatsResponse
import com.nauh.waterqualitymonitor.data.network.RetrofitClient
import com.nauh.waterqualitymonitor.ui.components.TopBar
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

data class Measurement(
    val id: Int,
    val timestamp: String,
    val value: Double,
    val status: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardDetail(navController: NavController, dashboardType: String) {
    var measurements by remember { mutableStateOf(listOf<Measurement>()) }
    var counter by remember { mutableStateOf(1) } // Biến đếm bắt đầu từ 1
    val apiService = RetrofitClient.apiService

    // Cập nhật dữ liệu mỗi 10 giây
    LaunchedEffect(Unit) {
        while (true) {
//            val newMeasurement = generateMeasurement(dashboardType, counter) // Truyền giá trị counter
//            measurements = listOf(newMeasurement) + measurements // Thêm dữ liệu mới vào đầu danh sách
//            counter++ // Tăng số thứ tự sau khi thêm
            apiService.getDatas().enqueue(object : retrofit2.Callback<StatsResponse> {
                override fun onResponse(
                    call: retrofit2.Call<StatsResponse>,
                    response: retrofit2.Response<StatsResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.metadata?.data
                        if (data != null) {
                            measurements = data.mapIndexed { index, statData ->
                                Measurement(
                                    id = index + 1,
                                    timestamp = statData.createdAt,
                                    value = when (dashboardType) {
                                        "turbidity" -> statData.tds.toDouble()
                                        "temperature" -> statData.temperature.toDouble()
                                        "relay" -> statData.relay.toDouble()
                                        else -> 0.0
                                    },
                                    status = when (dashboardType) {
                                        "turbidity" -> "Bình thường"
                                        "temperature" -> "Bình thường"
                                        "relay" -> if (statData.relay == 0) "Tắt" else "Bật"
                                        else -> "Không xác định"
                                    },
                                    color = when (dashboardType) {
                                        "turbidity" -> Color(0xFFE0F7FA)
                                        "temperature" -> Color.White
                                        "relay" -> if (statData.relay == 0) Color(0xFFFFEBEE) else Color(0xFFC8E6C9)
                                        else -> Color.White
                                    }
                                )
                            }
                        }
                    }
            }
                override fun onFailure(call: retrofit2.Call<StatsResponse>, t: Throwable) {
                    // Xử lý lỗi
                }
            })
            delay(1000) // Đợi 1 giây
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                pageTitle = "Chi tiết $dashboardType",
                onBackClick = {
                    navController.popBackStack()
                },
                onAccountClick = {
                    // Hành động khi nhấn vào biểu tượng tài khoản
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Hiển thị bảng dữ liệu
                MeasurementTable(measurements)
            }
        }
    }
}

@Composable
fun MeasurementTable(measurements: List<Measurement>) {
    // Tiêu đề bảng với đường phân cách dưới
    Column {
        // Tiêu đề của bảng
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), // Thêm padding để tạo khoảng cách cho tiêu đề
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "STT",
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.primary, // Thay đổi màu chữ để nổi bật
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold) // Tăng độ đậm
            )
            Text(
                "Thời Gian",
                modifier = Modifier.weight(2f),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                "Dữ Liệu Đo",
                modifier = Modifier.weight(2f),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                "Trạng Thái",
                modifier = Modifier.weight(2f),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Thêm đường phân cách dưới tiêu đề
        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), thickness = 1.dp)

        // Hiển thị danh sách các hàng với đường phân cách giữa các hàng
        LazyColumn {
            items(measurements) { measurement ->
                MeasurementRow(measurement)
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), thickness = 1.dp)
            }
        }
    }
}


@Composable
fun MeasurementRow(measurement: Measurement) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(measurement.color),  // Sử dụng màu sắc từ measurement
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(measurement.id.toString(), modifier = Modifier.weight(1f))
        Text(measurement.timestamp, modifier = Modifier.weight(2f))
        Text(measurement.value.toString(), modifier = Modifier.weight(2f))
        Text(measurement.status, modifier = Modifier.weight(2f))
    }
}

// Hàm để sinh dữ liệu đo
fun generateMeasurement(dashboardType: String, id: Int): Measurement { // Thêm id là tham số
    val randomValue = when (dashboardType) {
        "turbidity" -> (0..150).random().toDouble() // Đảm bảo tên khớp với route
        "temperature" -> (0..100).random().toDouble()
        "relay" -> (0..1).random().toDouble()
        else -> 0.0
    }

    val (status, color) = when (dashboardType) {
        "turbidity" -> when {
            randomValue <= 5 -> "Bình thường" to Color(0xFFE0F7FA)
            randomValue <= 10 -> "Cao" to Color(0xFFFFE0B2)
            else -> "Rất cao" to Color(0xFFFFCDD2)
        }
        "temperature" -> when {
            randomValue < 20 -> "Lạnh" to Color(0xFFBBDEFB)
            randomValue in 20.0..60.0 -> "Bình thường" to Color.White
            else -> "Nóng" to Color(0xFFFFC107)
        }
        "relay" -> when {
            randomValue == 0.0 -> "Tắt" to Color(0xFFFFEBEE)
            randomValue == 1.0 -> "Bật" to Color(0xFFC8E6C9)
            else -> "Không xác định" to Color.White
        }
        else -> "Không xác định" to Color.White
    }

    val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    return Measurement(id = id, timestamp = timestamp, value = randomValue, status = status, color = color)
}