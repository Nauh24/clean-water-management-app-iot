package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.R
import com.nauh.waterqualitymonitor.ui.components.TopBar
import com.nauh.waterqualitymonitor.ui.theme.Shapes
import com.nauh.waterqualitymonitor.ui.theme.Typography
import com.nauh.waterqualitymonitor.viewmodels.DashboardViewModel

@Composable
fun Dashboard(navController: NavController, viewModel: DashboardViewModel = viewModel()) {
    val dashboardData by viewModel.dashboardData.collectAsState()

    val turbidityThreshold = stringResource(id = R.string.threshold_tds).toDouble() // Chuyển từ String thành Int
    val temperatureThreshold = stringResource(id = R.string.threshold_temperature).toDouble()
    val warningColor = colorResource(id = R.color.warning)

    Scaffold(
        topBar = {
            TopBar(pageTitle = "Tổng quan", onAccountClick = {})
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardCardView(
                    title = "Độ Đục",
                    value = dashboardData.turbidity,
                    route = "dashboard_detail/turbidity",
                    onClick = { navController.navigate("dashboard_detail/turbidity") },
                    color = if (dashboardData.turbidity.split(" ")[0].toDouble() > turbidityThreshold) warningColor else MaterialTheme.colorScheme.primary
                )
                DashboardCardView(
                    title = "Nhiệt Độ",
                    value = dashboardData.temperature,
                    route = "dashboard_detail/temperature",
                    onClick = { navController.navigate("dashboard_detail/temperature") },
                    color = if (dashboardData.temperature.split("°")[0].toDouble() > temperatureThreshold) warningColor else MaterialTheme.colorScheme.primary
                )
                DashboardCardView(
                    title = "Trạng Thái Relay",
                    value = dashboardData.relayStatus,
                    route = "dashboard_detail/relay",
                    onClick = { navController.navigate("dashboard_detail/relay") },
                    color = if (dashboardData.relayStatus == "Bật") warningColor else MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
fun DashboardCardView(title: String, value: String, route: String, onClick: () -> Unit, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = Shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = Typography.titleMedium
            )
            Text(
                text = value,
                style = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}
