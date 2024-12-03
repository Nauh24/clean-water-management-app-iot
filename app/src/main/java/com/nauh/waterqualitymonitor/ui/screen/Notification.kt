package com.nauh.waterqualitymonitor.ui.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.R
import com.nauh.waterqualitymonitor.data.model.Alert
import com.nauh.waterqualitymonitor.ui.components.TopBar
import com.nauh.waterqualitymonitor.utils.AlertSaver
import com.nauh.waterqualitymonitor.utils.DataSaver
import com.nauh.waterqualitymonitor.utils.formatTimestamp
import com.nauh.waterqualitymonitor.viewmodels.NotificationViewModel
import kotlinx.coroutines.delay

//@Composable
//fun Notification(
//    navController: NavController,
//) {
//    // Danh sách thông báo được nhớ (state)
//    var alerts by remember { mutableStateOf(listOf<Alert>()) }
//    val alertSaver = AlertSaver(LocalContext.current) // Khởi tạo AlertSaver
//    val fileName = stringResource(id = R.string.alert_file_name)
//
//    // Đọc dữ liệu từ file khi màn hình được tải
//    LaunchedEffect(Unit) {
//        val loadedAlerts =
//            alertSaver.readAlertsFromFile(fileName)?.takeLast(20)?.reversed() // Đọc thông báo từ file
//        if (loadedAlerts != null) {
//            alerts = loadedAlerts // Cập nhật danh sách thông báo
//            Log.d("Notification", "Loaded ${alerts.size} alerts from file.")
//        } else {
//            Log.d("Notification", "No alerts found in file.")
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopBar(
//                pageTitle = "Thông báo",
//                onAccountClick = {
//                    navController.navigate("login")
//                }
//            )
//        },
//        content = { innerPadding ->
//            Surface(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding),
//                color = MaterialTheme.colorScheme.background
//            ) {
//                Column(modifier = Modifier.fillMaxSize()) {
//                    NotificationList(navController, alerts) // Hiển thị danh sách thông báo
//                }
//            }
//        }
//    )
//}

@Composable
fun Notification(navController: NavController) {
    var alerts by remember { mutableStateOf(listOf<Alert>()) }
    val alertSaver = AlertSaver(LocalContext.current)
    val fileName = stringResource(id = R.string.alert_file_name)

    // Đọc dữ liệu từ file khi màn hình được tải
    LaunchedEffect(Unit) {
        val loadedAlerts =
            alertSaver.readAlertsFromFile(fileName)?.takeLast(20)?.reversed()
        if (loadedAlerts != null) {
            alerts = loadedAlerts // Cập nhật danh sách thông báo
            Log.d("Notification", "Loaded ${alerts.size} alerts from file.")
        } else {
            Log.d("Notification", "No alerts found in file.")
        }
    }

    // Lắng nghe sự kiện khi thông báo mới được lưu vào file
    // Chức năng này sẽ được gọi trong AlertSaver khi có thông báo mới
    // Và gọi lại `readAlertsFromFile` để cập nhật danh sách thông báo.

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
                Column(modifier = Modifier.fillMaxSize()) {
                    NotificationList(navController, alerts)
                }
            }
        }
    )
}


@Composable
fun NotificationList(navController: NavController, alerts: List<Alert>) {
    if (alerts.isEmpty()) {
        // Hiển thị vòng tròn tải khi không có dữ liệu
        LoadingIndicator()
    } else {
        // Hiển thị danh sách thông báo bằng LazyColumn
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(alerts) { alert ->
                NotificationCard(alert = alert) {
                    // Điều hướng đến màn hình chi tiết khi bấm vào thông báo
//                    navController.navigate("notification_detail")
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
        CircularProgressIndicator() // Vòng tròn tải
    }
}

@Composable
fun NotificationCard(alert: Alert, onClick: () -> Unit) {
    val (date, time) = formatTimestamp(alert.createdAt) // Tách ngày và giờ từ timestamp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Hiển thị loại cảnh báo
//            Text(
//                text = alert.alertType,
//                style = MaterialTheme.typography.titleMedium
//            )
            // Hiển thị thông điệp
            Text(
                text = alert.message,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
            )
            // Hiển thị thời gian
            Column {
                Text(text = "Ngày: $date", style = MaterialTheme.typography.bodySmall)
                Text(text = "Giờ: $time", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
