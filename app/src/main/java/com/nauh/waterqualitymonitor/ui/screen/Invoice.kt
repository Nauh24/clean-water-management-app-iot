package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun Invoice(navController: NavController) {
    // Mock data for demonstration
    val customerName = "Nguyễn Văn Huân"
    val address = "Thụy Ninh, Thái Thụy, Thái Bình"
    val oldWaterIndex = 232
    val currentWaterIndex = 239
    val costPerCubicMeter = 28000.0

    // Tính toán
    val totalWaterConsumed = currentWaterIndex - oldWaterIndex  // Tổng lượng nước tiêu thụ
    val totalCost = (totalWaterConsumed / 1000) * costPerCubicMeter  // Tính tổng chi phí

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Hóa Đơn Tiền Nước",
                style = MaterialTheme.typography.headlineMedium
            )

            // Hiển thị thông tin khách hàng
            Text(
                text = "Tên khách hàng: $customerName",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Địa chỉ: $address",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị chỉ số nước
            Text(
                text = "Chỉ số nước cũ: $oldWaterIndex L",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Chỉ số nước hiện tại: $currentWaterIndex L",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị thông tin tiêu thụ nước và chi phí
            Text(
                text = "Tổng lượng nước tiêu thụ: $totalWaterConsumed L",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Đơn giá: ${"%.2f".format(costPerCubicMeter)} VNĐ/m³",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Tổng thành tiền: ${"%.2f".format(totalCost)} VNĐ",
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}
