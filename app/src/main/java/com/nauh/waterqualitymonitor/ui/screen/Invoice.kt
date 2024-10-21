package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.ui.components.TopBar

data class InvoiceData(
    val customerName: String,
    val address: String,
    val oldWaterIndex: Int,
    val currentWaterIndex: Int,
    val costPerCubicMeter: Double
) {
    val totalWaterConsumed: Int
        get() = currentWaterIndex - oldWaterIndex

    val totalCost: Double
        get() = totalWaterConsumed * costPerCubicMeter
}

@Composable
fun Invoice(navController: NavController) {
    // Dữ liệu giả lập cho demo
    val invoiceData = InvoiceData(
        customerName = "Nguyễn Văn Huân",
        address = "Thụy Ninh, Thái Thụy, Thái Bình",
        oldWaterIndex = 232,
        currentWaterIndex = 239,
        costPerCubicMeter = 28000.0
    )

    Scaffold(
        topBar = { TopBar(pageTitle = "Hóa Đơn", onAccountClick = {}) }
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

                CustomerInfoCard(invoiceData)
                WaterIndexCard(invoiceData)
                ConsumptionInfoCard(invoiceData)
            }
        }
    }
}

@Composable
fun CustomerInfoCard(invoiceData: InvoiceData) {
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
                text = "Tên khách hàng: ${invoiceData.customerName}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Địa chỉ: ${invoiceData.address}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun WaterIndexCard(invoiceData: InvoiceData) {
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
                text = "CS cũ: ${invoiceData.oldWaterIndex} L",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "CS mới: ${invoiceData.currentWaterIndex} L",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ConsumptionInfoCard(invoiceData: InvoiceData) {
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
                text = "Tiêu thụ: ${invoiceData.totalWaterConsumed} L",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Đơn giá: ${"%.2f".format(invoiceData.costPerCubicMeter)} VNĐ/m³",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Tổng thành tiền: ${"%.2f".format(invoiceData.totalCost)} VNĐ",
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}
