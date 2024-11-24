package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.data.model.Alert
import com.nauh.waterqualitymonitor.ui.components.TopBar
import com.nauh.waterqualitymonitor.utils.formatTimestamp
import com.nauh.waterqualitymonitor.viewmodels.NotificationViewModel

@Composable
fun Notification(navController: NavController, viewModel: NotificationViewModel = NotificationViewModel()) {
    val alerts by viewModel.alerts.collectAsState()

    // Đảm bảo luôn hiển thị dữ liệu mới khi có thay đổi
    Scaffold(
        topBar = {
            TopBar(
                pageTitle = "Thông báo",
                onAccountClick = {
                    navController.navigate("login")
                }
            )
        },
        content = { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                NotificationList(navController, alerts)
            }
        }
    )
}

@Composable
fun NotificationList(navController: NavController, alerts: List<Alert>) {
    // Nếu dữ liệu chưa được tải, hiển thị một loading indicator
    if (alerts.isEmpty()) {
        LoadingIndicator()
    } else {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(alerts.reversed()) { alert ->
                NotificationCard(alert = alert) {
                    navController.navigate("notification_detail/${alert.id}")
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun NotificationCard(alert: Alert, onClick: () -> Unit) {
    val (date, time) = formatTimestamp(alert.createdAt)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = alert.alertType,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = alert.message,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
            )
            // Hiển thị ngày và giờ trên 2 dòng
            Column {
                Text(text = "Ngày: $date", style = MaterialTheme.typography.bodySmall)
                Text(text = "Giờ: $time", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
