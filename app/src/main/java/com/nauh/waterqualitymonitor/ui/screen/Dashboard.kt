package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.ui.components.TopBar
import com.nauh.waterqualitymonitor.ui.theme.Shapes
import com.nauh.waterqualitymonitor.ui.theme.Typography

data class DashboardCard(
    val title: String,
    val value: String,
    val route: String
)

val dashboardCards = listOf(
    DashboardCard("Độ Đục", "5 NTU", "dashboard_detail/turbidity"),
    DashboardCard("Độ Dẫn Điện (EC)", "200 µS/cm", "dashboard_detail/ec"),
    DashboardCard("Nhiệt Độ", "25°C", "dashboard_detail/temperature"),
    DashboardCard("Trạng Thái Relay", "Bật", "dashboard_detail/relay")
)

@Composable
fun Dashboard(navController: NavController) {
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
                dashboardCards.forEach { card ->
                    DashboardCardView(card = card) {
                        navController.navigate(card.route)
                    }
                }
            }
        }
    )
}

@Composable
fun DashboardCardView(card: DashboardCard, onClick: () -> Unit) {
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
                text = card.title,
                style = Typography.titleMedium
            )
            Text(
                text = card.value,
                style = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}
