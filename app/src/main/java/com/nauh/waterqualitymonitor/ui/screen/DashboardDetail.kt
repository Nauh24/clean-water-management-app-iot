package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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

    // Cập nhật dữ liệu mỗi 10 giây
    LaunchedEffect(Unit) {
        while (true) {
            val newMeasurement = generateMeasurement(dashboardType)
            measurements = listOf(newMeasurement) + measurements // Thêm dữ liệu mới vào đầu danh sách
            delay(1000) // Đợi 10 giây
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                pageTitle = "Chi tiết $dashboardType",
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
                Text(
                    text = "Bảng Thống Kê $dashboardType",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Hiển thị bảng dữ liệu
                MeasurementTable(measurements)
            }
        }
    }
}

@Composable
fun MeasurementTable(measurements: List<Measurement>) {
    // Tiêu đề bảng
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("STT", modifier = Modifier.weight(1f))
            Text("Thời Gian", modifier = Modifier.weight(2f))
            Text("Dữ Liệu Đo", modifier = Modifier.weight(2f))
            Text("Trạng Thái", modifier = Modifier.weight(2f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Duyệt qua danh sách dữ liệu và hiển thị
        LazyColumn {
            items(measurements) { measurement ->
                MeasurementRow(measurement)
            }
        }
    }
}

@Composable
fun MeasurementRow(measurement: Measurement) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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
fun generateMeasurement(dashboardType: String): Measurement {
    val randomValue = when (dashboardType) {
        "Độ đục" -> (0..15).random().toDouble()
        "Độ dẫn điện" -> (0..1500).random().toDouble()
        "Nhiệt độ" -> (0..100).random().toDouble()
        "Relay" -> {
            // Giả sử giá trị relay không có dữ liệu đo cụ thể
            0.0
        }
        else -> 0.0
    }

    val (status, color) = when (dashboardType) {
        "Độ đục" -> when {
            randomValue <= 5 -> "Bình thường" to Color(0xFFE0F7FA)
            randomValue <= 10 -> "Cao" to Color(0xFFFFE0B2)
            else -> "Rất cao" to Color(0xFFFFCDD2)
        }
        "Độ dẫn điện" -> when {
            randomValue <= 500 -> "Bình thường" to Color(0xFFE0F7FA)
            randomValue <= 1000 -> "Cao" to Color(0xFFFFE0B2)
            else -> "Rất cao" to Color(0xFFFFCDD2)
        }
        "Nhiệt độ" -> when {
            randomValue < 20 -> "Lạnh" to Color(0xFFBBDEFB)
            randomValue in 20.0..60.0 -> "Bình thường" to Color.White
            else -> "Nóng" to Color(0xFFFFC107)
        }
        "Relay" -> {
            // Chưa có dữ liệu đo cụ thể
            "Trạng thái" to Color.White
        }
        else -> "Không xác định" to Color.White
    }

    val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    return Measurement(id = (1..1000).random(), timestamp = timestamp, value = randomValue, status = status, color = color)
}
