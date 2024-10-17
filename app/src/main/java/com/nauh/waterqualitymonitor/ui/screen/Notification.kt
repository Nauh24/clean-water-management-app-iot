package com.nauh.waterqualitymonitor.ui.screen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Data class for Notification
data class Notification(val id: Int, val title: String, val description: String, val turbidity: Int)

// Sample notifications
val notifications = listOf(
    Notification(1, "Ngắt nước do độ đục cao", "Hệ thống đã ngắt nước do độ đục vượt quá ngưỡng cho phép.", 10),
    Notification(2, "Cảnh báo nhiệt độ", "Nhiệt độ nước đã vượt ngưỡng an toàn.", 0),
    Notification(3, "Ngắt nước do độ dẫn cao", "Hệ thống đã ngắt nước do độ dẫn điện vượt ngưỡng cho phép.", 0)
)

@Composable
fun Notification(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Danh sách thông báo",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // List of notifications
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(notifications) { notification ->
                    NotificationCard(notification = notification) {
                        // Navigate to detail screen when clicked
                        navController.navigate("notification_detail/${notification.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(notification: Notification, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },  // Handle click event
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = notification.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = notification.description,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}
