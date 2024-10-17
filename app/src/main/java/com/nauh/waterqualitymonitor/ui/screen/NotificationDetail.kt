package com.nauh.waterqualitymonitor.ui.screen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun NotificationDetail(navController: NavController, notificationId: Int) {
    // Find the notification by ID
    val notification = notifications.firstOrNull { it.id == notificationId }

    if (notification != null) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Chi tiết thông báo",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tiêu đề: ${notification.title}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Mô tả: ${notification.description}",
                    style = MaterialTheme.typography.bodyLarge
                )

                // Hiển thị chi tiết thông số độ đục nếu có
                if (notification.turbidity > 0) {
                    Text(
                        text = "Thông số độ đục: ${notification.turbidity} NTU",
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    } else {
        Text(text = "Thông báo không tồn tại.")
    }
}
