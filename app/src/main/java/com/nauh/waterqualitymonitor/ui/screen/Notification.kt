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

    Scaffold(
        topBar = {
            TopBar(
                pageTitle = "Thông báo",
                onAccountClick = {
                    // Xử lý sự kiện khi nhấn vào tài khoản nếu cần
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
                if (alerts.isEmpty()) {
                    EmptyNotificationMessage()
                } else {
                    NotificationList(navController, alerts)
                }
            }
        }
    )
}

@Composable
fun NotificationList(navController: NavController, alerts: List<Alert>) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(alerts.reversed()) { alert ->
            NotificationCard(alert = alert) {
                // Điều hướng đến màn hình chi tiết khi nhấn vào
                navController.navigate("notification_detail/${alert.id}")
            }
        }
    }
}

@Composable
fun EmptyNotificationMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Không có thông báo nào.",
            style = MaterialTheme.typography.bodyLarge
        )
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