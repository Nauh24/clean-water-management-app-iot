package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DashboardDetail(navController: NavController, chartType: String) {
    val data = remember { getDataForChartType(chartType) } // Lấy dữ liệu theo loại biểu đồ
    var selectedType by remember { mutableStateOf(chartType) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Nút lọc chọn loại bảng dữ liệu
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { selectedType = "turbidity" }) {
                    Text("Độ đục")
                }
                Button(onClick = { selectedType = "ec" }) {
                    Text("EC")
                }
                Button(onClick = { selectedType = "temperature" }) {
                    Text("Nhiệt độ")
                }
                Button(onClick = { selectedType = "relay" }) {
                    Text("Relay")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị bảng dữ liệu
            DataTable(data = data, selectedType = selectedType)
        }
    }
}

// Màn hình hiển thị bảng dữ liệu
@Composable
fun DataTable(data: List<DataRow>, selectedType: String) {
    val headers = listOf("Số thứ tự", "Dữ liệu đo", "Thời gian", "Trạng thái")
    val threshold = getThresholdForType(selectedType) // Lấy ngưỡng giá trị cho loại bảng hiện tại

    Column {
        // Hiển thị tiêu đề bảng
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp)
        ) {
            headers.forEach { header ->
                Text(
                    text = header,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onPrimary)
                )
            }
        }

        // Hiển thị các hàng dữ liệu
        data.forEachIndexed { index, row ->
            val rowColor = if (row.value > threshold.max || row.value < threshold.min) {
                MaterialTheme.colorScheme.error // Nếu vượt ngưỡng, đổi màu hàng
            } else {
                MaterialTheme.colorScheme.surface // Màu mặc định
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(rowColor)
                    .padding(8.dp)
            ) {
                Text(text = "${index + 1}", modifier = Modifier.weight(1f))
                Text(text = "${row.value}", modifier = Modifier.weight(1f))
                Text(text = row.time, modifier = Modifier.weight(1f))
                Text(text = row.status, modifier = Modifier.weight(1f))
            }
        }
    }
}

// Lớp dữ liệu mẫu để hiển thị
data class DataRow(val value: Int, val time: String, val status: String)

// Hàm giả lập để lấy dữ liệu dựa trên loại bảng hiện tại
fun getDataForChartType(type: String): List<DataRow> {
    return when (type) {
        "turbidity" -> listOf(
            DataRow(6, "2024-10-10 12:00", "Bình thường"),
            DataRow(11, "2024-10-10 13:00", "Ngưỡng cao"),
            DataRow(3, "2024-10-10 14:00", "Ngưỡng thấp")
        )
        "ec" -> listOf(
            DataRow(150, "2024-10-10 12:00", "Bình thường"),
            DataRow(500, "2024-10-10 13:00", "Ngưỡng cao"),
            DataRow(100, "2024-10-10 14:00", "Ngưỡng thấp")
        )
        "temperature" -> listOf(
            DataRow(25, "2024-10-10 12:00", "Bình thường"),
            DataRow(35, "2024-10-10 13:00", "Nóng"),
            DataRow(15, "2024-10-10 14:00", "Lạnh")
        )
        else -> listOf()
    }
}

// Cấu trúc ngưỡng giá trị cho từng loại bảng
data class Threshold(val min: Int, val max: Int)

fun getThresholdForType(type: String): Threshold {
    return when (type) {
        "turbidity" -> Threshold(min = 5, max = 10)
        "ec" -> Threshold(min = 100, max = 400)
        "temperature" -> Threshold(min = 20, max = 30)
        else -> Threshold(min = 0, max = Int.MAX_VALUE)
    }
}
