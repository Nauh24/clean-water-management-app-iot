package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.ui.components.TopBar

@Composable
fun Invoice(navController: NavController, username: String) {
    // Dữ liệu giả lập cho demo
    val customerName = "Nguyễn Văn Huân"
    val address = "Thụy Ninh, Thái Thụy, Thái Bình"
    val oldWaterIndex = 232
    val currentWaterIndex = 239
    val costPerCubicMeter = 28000.0

    // Tính toán
    val totalWaterConsumed = currentWaterIndex - oldWaterIndex  // Tổng lượng nước tiêu thụ
    val totalCost = totalWaterConsumed * costPerCubicMeter  // Tính tổng chi phí

    Scaffold(
        topBar = { TopBar(username = username, onAccountClick = { /* Thêm hành động khi nhấn vào biểu tượng tài khoản */ }) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Đảm bảo nội dung không bị che khuất bởi TopBar
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

                // Hiển thị thông tin khách hàng trong Card
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Thông tin khách hàng",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Tên khách hàng: $customerName",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Địa chỉ: $address",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hiển thị chỉ số nước trong Card
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Chỉ số nước",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "CS cũ: $oldWaterIndex L",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "CS mới: $currentWaterIndex L",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hiển thị thông tin tiêu thụ nước và chi phí trong Card
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Thông tin tiêu thụ",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Tiêu thụ: $totalWaterConsumed L",
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
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
