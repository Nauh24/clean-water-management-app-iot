package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.R
import com.nauh.waterqualitymonitor.ui.components.TopBar
import com.nauh.waterqualitymonitor.utils.DataSaver
import com.nauh.waterqualitymonitor.utils.formatTimestamp
import kotlinx.coroutines.delay

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
    val dataSaver = DataSaver(LocalContext.current) // Khởi tạo DataSaver

    // Truy xuất các giá trị từ strings.xml
    val turbidityThreshold = stringResource(id = R.string.threshold_tds).toDouble()
    val temperatureThreshold = stringResource(id = R.string.threshold_temperature).toDouble()
    val delayTime = stringResource(id = R.string.delay_time).toLong()
    val fileName = stringResource(id = R.string.file_name)
    val warningColor = colorResource(id = R.color.warning)
    val relayColor = colorResource(id = R.color.green)

    // Dữ liệu mới được cập nhật mỗi khi có thay đổi
    LaunchedEffect(Unit) {
        while (true) {
            // Đọc và cập nhật dữ liệu từ file sau mỗi `delayTime` mili giây
            val dataFromFile = dataSaver.readDataFromFile(fileName)

            if (dataFromFile != null) {
                // Sắp xếp và lấy 100 bản ghi mới nhất
                val latestMeasurements = dataFromFile.sortedByDescending { it.createdAt }.take(100)

                // Cập nhật measurements
                measurements = latestMeasurements.mapIndexed { index, statData ->
                    Measurement(
                        id = index + 1,
                        timestamp = statData.createdAt,
                        value = when (dashboardType) {
                            "turbidity" -> statData.tds.toDouble()
                            "temperature" -> statData.temperature.toDouble()
                            "flow_rate" -> statData.flowRate.toDouble()
                            "relay" -> statData.relay.toDouble()
                            else -> 0.0
                        },
                        status = when (dashboardType) {
                            "turbidity" -> if (statData.tds >= turbidityThreshold) "Cao" else "Bình thường"
                            "temperature" -> if (statData.temperature >= temperatureThreshold) "Nóng" else "Bình thường"
                            "relay" -> if (statData.relay == 0) "Tắt" else "Bật"
                            else -> ""
                        },
                        color = when (dashboardType) {
                            "turbidity" -> if (statData.tds >= turbidityThreshold) warningColor else Color.White
                            "temperature" -> if (statData.temperature >= temperatureThreshold) warningColor else Color.White
                            "relay" -> if (statData.relay == 0) warningColor else relayColor
                            else -> Color.White
                        }
                    )
                }
            }
            delay(delayTime) // Cập nhật theo thời gian delayTime
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                pageTitle = "Chi tiết $dashboardType",
                onBackClick = { navController.popBackStack() },
                onAccountClick = {}
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
            itemsIndexed(measurements) { index, measurement -> // Sử dụng itemsIndexed
                MeasurementRow(
                    measurement = measurement,
                    index = index + 1
                ) // Đưa chỉ số bắt đầu từ 1
                Divider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    thickness = 1.dp
                )
            }
        }
    }
}


@Composable
fun MeasurementRow(measurement: Measurement, index: Int) {
    val (date, time) = formatTimestamp(measurement.timestamp)

    // Kiểm tra xem giá trị có vượt ngưỡng không để áp dụng màu chữ trắng
    val textColor = if (measurement.color != Color.White) Color.White else Color.Gray
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(measurement.color),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            index.toString(), // Sử dụng chỉ số được truyền vào
            modifier = Modifier.weight(1f),
            color = textColor
        )

        // Hiển thị ngày và giờ trên 2 dòng
        Column(modifier = Modifier.weight(2f)) {
            Text(date, color = textColor)
            Text(time, color = textColor)
        }

        Text(measurement.value.toString(), modifier = Modifier.weight(2f), color = textColor)
        Text(measurement.status, modifier = Modifier.weight(2f), color = textColor)
    }
}

