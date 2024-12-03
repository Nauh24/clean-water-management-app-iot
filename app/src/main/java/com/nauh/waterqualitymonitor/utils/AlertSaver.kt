package com.nauh.waterqualitymonitor.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.nauh.waterqualitymonitor.data.model.Alert
import java.io.File

class AlertSaver(private val context: Context) {

    /**
     * Lưu một thông báo mới từ WebSocket.
     * @param message Dữ liệu JSON dạng chuỗi.
     * @param fileName Tên file lưu trữ.
     */
    fun saveNewAlert(message: String, fileName: String) {
        try {
            val gson = Gson()
            val newAlert = gson.fromJson(message, Alert::class.java)

            // Đọc dữ liệu hiện tại từ file
            val existingAlerts = readAlertsFromFile(fileName)?.toMutableList() ?: mutableListOf()

            existingAlerts.add(newAlert)
            saveAlertsToFile(existingAlerts, fileName)
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
}
