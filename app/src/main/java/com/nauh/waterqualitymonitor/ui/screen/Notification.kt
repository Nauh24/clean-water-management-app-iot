package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.ui.components.TopBar // Import TopBar

// Data class for Notification
data class Notification(val id: Int, val title: String, val description: String, val turbidity: Int)

// Sample notifications
val notifications = listOf(
    Notification(1, "Ngắt nước do độ đục cao", "Hệ thống đã ngắt nước do độ đục vượt quá ngưỡng cho phép.", 10),
    Notification(2, "Cảnh báo nhiệt độ", "Nhiệt độ nước đã vượt ngưỡng an toàn.", 0),
    Notification(3, "Ngắt nước do độ dẫn cao", "Hệ thống đã ngắt nước do độ dẫn điện vượt ngưỡng cho phép.", 0)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notification(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(
                pageTitle = "Thông báo",  // Tiêu đề của trang
                onAccountClick = {
                    // Hành động khi nhấn vào icon tài khoản (có thể điều hướng hoặc xử lý logic khác)
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
                modifier = Modifier.padding(16.dp)
            ) {
                // Danh sách thông báo
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(notifications) { notification ->
                        NotificationCard(notification = notification) {
                            // Điều hướng tới trang chi tiết thông báo khi nhấn
                            navController.navigate("notification_detail/${notification.id}")
                        }
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
            .clickable { onClick() },  // Xử lý sự kiện khi nhấn vào
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
