package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.charts.ECChart
import com.nauh.waterqualitymonitor.charts.TemperatureChart
import com.nauh.waterqualitymonitor.charts.TurbidityChart
import com.nauh.waterqualitymonitor.charts.WaterBillChart
import com.nauh.waterqualitymonitor.ui.components.TopBar // Import TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Statistics(navController: NavController) {
    var selectedChart by remember { mutableStateOf("Turbidity") } // Biểu đồ mặc định là "Turbidity"
    val chartOptions = listOf("Turbidity", "Conductivity (EC)", "Temperature", "Water Bill")

    Scaffold(
        topBar = {
            TopBar(
                pageTitle = "Thống kê", // Tiêu đề của trang
                onAccountClick = {
                    // Hành động khi nhấn vào icon tài khoản
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Đảm bảo nội dung không bị che bởi TopBar
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Chọn loại biểu đồ",
                    style = MaterialTheme.typography.headlineMedium
                )

                // Dropdown để chọn loại biểu đồ
                var expanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { expanded = !expanded }) {
                        Text(text = selectedChart)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        chartOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedChart = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Hiển thị biểu đồ theo lựa chọn
                when (selectedChart) {
                    "Turbidity" -> {
                        TurbidityChart()
                    }

                    "Conductivity (EC)" -> {
                        ECChart()
                    }

                    "Temperature" -> {
                        TemperatureChart()
                    }

                    "Water Bill" -> {
                        WaterBillChart()
                    }
                }
            }
        }
    }
}
