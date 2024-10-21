package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.data.Notification
import com.nauh.waterqualitymonitor.data.mockNotifications
import com.nauh.waterqualitymonitor.ui.components.TopBar

@Composable
fun NotificationDetail(navController: NavController, notificationId: Int) {
    val notification = mockNotifications.firstOrNull { it.id == notificationId }

    Scaffold(
        topBar = {
            TopBar(
                pageTitle = "Chi tiết thông báo",
                username = "Nauh",
                onAccountClick = {
                    // Điều hướng khi nhấn vào tài khoản nếu cần
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
                if (notification != null) {
                    NotificationDetailContent(notification)
                } else {
                    NotificationNotFound()
                }
            }
        }
    )
}

@Composable
fun NotificationDetailContent(notification: Notification) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Tiêu đề: ${notification.title}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Mô tả: ${notification.description}",
            style = MaterialTheme.typography.bodyLarge
        )

        // Hiển thị thông số độ đục nếu có
        if (notification.turbidity > 0) {
            Text(
                text = "Thông số độ đục: ${notification.turbidity} NTU",
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
fun NotificationNotFound() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Thông báo không tồn tại.",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}
