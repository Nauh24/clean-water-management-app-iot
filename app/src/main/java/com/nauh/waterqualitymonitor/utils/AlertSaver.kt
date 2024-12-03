package com.nauh.waterqualitymonitor.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import com.nauh.waterqualitymonitor.MainActivity
import com.nauh.waterqualitymonitor.R
import com.nauh.waterqualitymonitor.data.model.Alert
import java.io.File

class AlertSaver(private val context: Context) {

    /**
     * Lưu một thông báo mới từ WebSocket.
     * @param message Dữ liệu JSON dạng chuỗi.
     * @param fileName Tên file lưu trữ.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveNewAlert(message: String, fileName: String) {
        try {
            val gson = Gson()
            val newAlert = gson.fromJson(message, Alert::class.java)

            // Đọc dữ liệu hiện tại từ file
            val existingAlerts = readAlertsFromFile(fileName)?.toMutableList() ?: mutableListOf()

            existingAlerts.add(newAlert)
            saveAlertsToFile(existingAlerts, fileName)

            // Hiển thị thông báo khi lưu thành công
            showNotification(newAlert.message)

            Log.d("AlertSaver", "New alert saved: $newAlert")
        } catch (e: Exception) {
            Log.e("AlertSaver", "Failed to save new alert: ${e.message}")
        }
    }

    /**
     * Lưu toàn bộ danh sách thông báo vào file JSON.
     */
    private fun saveAlertsToFile(alerts: List<Alert>, fileName: String) {
        try {
            val gson = Gson()
            val json = gson.toJson(alerts)
            val file = File(context.filesDir, fileName)
            file.writeText(json)
            Log.d("AlertSaver", "Alerts saved to file: $fileName")
        } catch (e: Exception) {
            Log.e("AlertSaver", "Error saving alerts to file: ${e.message}")
        }
    }

    /**
     * Đọc danh sách thông báo từ file JSON.
     * @param fileName Tên file JSON.
     * @return Danh sách thông báo hoặc null nếu không có dữ liệu.
     */
    fun readAlertsFromFile(fileName: String): List<Alert>? {
        return try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                Log.d("AlertSaver", "Reading alerts from file: $fileName")
                val json = file.readText()
                Gson().fromJson(json, Array<Alert>::class.java).toList()
            } else {
                Log.d("AlertSaver", "File not found: $fileName")
                null
            }
        } catch (e: Exception) {
            Log.e("AlertSaver", "Error reading alerts from file: ${e.message}")
            null
        }
    }

    /**
     * Đọc thông báo mới nhất từ file JSON.
     * @param fileName Tên file.
     * @return Phần tử mới nhất hoặc null nếu không có thông báo.
     */
    fun readLatestAlertFromFile(fileName: String): Alert? {
        val alerts = readAlertsFromFile(fileName)
        // Nếu có thông báo, trả về phần tử cuối cùng (mới nhất)
        return alerts?.lastOrNull()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(alertMessage: String) {
        val channelId = "water_quality_channel"
        val notificationId = System.currentTimeMillis().toInt()

        // Tạo Intent để mở NotificationActivity (hoặc fragment/tab trong ứng dụng)
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigateTo", "notification") // Đánh dấu rằng cần mở tab thông báo
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification_id)
            .setContentTitle("Cảnh báo chất lượng nước")
            .setContentText(alertMessage)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)  // Khi nhấn vào thông báo sẽ mở MainActivity và điều hướng đến tab thông báo
            .setAutoCancel(true)

        val channel = NotificationChannel(
            channelId,
            "Thông báo chất lượng nước",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Kênh thông báo về cảnh báo chất lượng nước"
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // Kiểm tra quyền gửi thông báo
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build()) // Gửi thông báo
            }
        } else {
            Log.e("AlertSaver", "Permission to post notifications is not granted")
        }
    }


}
