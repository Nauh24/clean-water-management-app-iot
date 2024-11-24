package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.charts.FlowRateChart
import com.nauh.waterqualitymonitor.charts.TemperatureChart
import com.nauh.waterqualitymonitor.charts.TurbidityChart
import com.nauh.waterqualitymonitor.charts.WaterBillChart
import com.nauh.waterqualitymonitor.ui.components.TopBar

enum class ChartType(val displayName: String) {
    TURBIDITY("Turbidity"),
    TEMPERATURE("Temperature"),
    FLOW_RATE("Flow Rate"),
    WATER_BILL("Water Bill")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Statistics(navController: NavController) {
    var selectedChart by remember { mutableStateOf(ChartType.TURBIDITY) } // Biểu đồ mặc định là "Turbidity"

    Scaffold(
        topBar = {
            TopBar(
                pageTitle = "Thống kê",
                onAccountClick = {
                    // Hành động khi nhấn vào icon tài khoản
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
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Dropdown để chọn loại biểu đồ
                DropdownChartMenu(
                    selectedChart = selectedChart,
                    onChartSelected = { selectedChart = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Hiển thị biểu đồ theo lựa chọn
                DisplayChart(selectedChart)
            }
        }
    }
}

@Composable
fun DropdownChartMenu(selectedChart: ChartType, onChartSelected: (ChartType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { expanded = !expanded }) {
            Text(text = selectedChart.displayName)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ChartType.values().forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.displayName) },
                    onClick = {
                        onChartSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DisplayChart(selectedChart: ChartType) {
    when (selectedChart) {
        ChartType.TURBIDITY -> TurbidityChart()
        ChartType.TEMPERATURE -> TemperatureChart()
        ChartType.FLOW_RATE -> FlowRateChart()
        ChartType.WATER_BILL -> WaterBillChart()
    }
}
