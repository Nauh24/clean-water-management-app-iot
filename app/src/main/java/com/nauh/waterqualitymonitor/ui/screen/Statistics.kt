package com.nauh.waterqualitymonitor.ui.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.nauh.waterqualitymonitor.charts.ECChart
import com.nauh.waterqualitymonitor.charts.TemperatureChart
import com.nauh.waterqualitymonitor.charts.TurbidityChart
import com.nauh.waterqualitymonitor.charts.WaterBillChart

@Composable
fun Statistics(navController: NavController) {
    var selectedChart by remember { mutableStateOf("Turbidity") } // Biểu đồ mặc định là "Turbidity"
    val chartOptions = listOf("Turbidity", "Conductivity (EC)", "Temperature", "Water Bill")

    Surface(
        modifier = Modifier.fillMaxSize(),
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

            // Dropdown to select chart type
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

