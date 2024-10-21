package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.data.Notification
import com.nauh.waterqualitymonitor.data.mockNotifications
import com.nauh.waterqualitymonitor.ui.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notification(navController: NavController) {
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
                NotificationList(navController)
            }
        }
    )
}

@Composable
fun NotificationList(navController: NavController) {
    if (mockNotifications.isEmpty()) {
        EmptyNotificationMessage()
    } else {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(mockNotifications) { notification ->
                NotificationCard(notification = notification) {
                    // Điều hướng đến màn hình chi tiết khi nhấn vào
                    navController.navigate("notification_detail/${notification.id}")
                }
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
fun NotificationCard(notification: Notification, onClick: () -> Unit) {
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
