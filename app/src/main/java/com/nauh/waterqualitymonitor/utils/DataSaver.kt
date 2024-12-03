package com.nauh.waterqualitymonitor.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.nauh.waterqualitymonitor.data.model.StatData
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class DataSaver(private val context: Context) {

    /**
     * Lưu dữ liệu mới từ WebSocket.
     * @param message Dữ liệu JSON dạng chuỗi.
     * @param fileName Tên file lưu trữ.
     */
//    fun saveNewData(message: String, fileName: String) {
//        try {
//            val gson = Gson()
//            val newData = gson.fromJson(message, StatData::class.java) // Parse JSON thành StatData object
//            val existingData = readDataFromFile(fileName)?.toMutableList() ?: mutableListOf()
//            existingData.add(newData) // Thêm dữ liệu mới
//            saveDataToFile(existingData, fileName) // Lưu danh sách vào file
//            Log.d("DataSaver", "New data saved: $newData")
//        } catch (e: Exception) {
//            Log.e("DataSaver", "Failed to save new data: ${e.message}")
//        }
//    }

    fun saveNewData(message: String, fileName: String) {
        try {
            val gson = Gson()
            val newData = gson.fromJson(message, StatData::class.java) // Parse JSON thành StatData object

            // Thêm thời gian hiện tại vào trường createdAt
//            val currentTimestamp = System.currentTimeMillis() // Lấy thời gian hiện tại từ hệ thống
//            val timestampFormatted = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(currentTimestamp)
//            newData.createdAt = timestampFormatted // Gán thời gian vào trường createdAt

            // Đọc dữ liệu cũ từ file và thêm dữ liệu mới
            val existingData = readDataFromFile(fileName)?.toMutableList() ?: mutableListOf()
            existingData.add(newData) // Thêm dữ liệu mới vào danh sách

            // Lưu lại toàn bộ dữ liệu vào file
            saveDataToFile(existingData, fileName)
            Log.d("DataSaver", "New data saved: $newData")
        } catch (e: Exception) {
            Log.e("DataSaver", "Failed to save new data: ${e.message}")
        }
    }



    /**
     * Lưu toàn bộ danh sách dữ liệu vào file JSON.
     */
    private fun saveDataToFile(data: List<StatData>, fileName: String) {
        try {
            val gson = Gson()
            val json = gson.toJson(data)
            val file = File(context.filesDir, fileName)
            file.writeText(json)
            Log.d("DataSaver", "Data saved to file: $fileName")
        } catch (e: Exception) {
            Log.e("DataSaver", "Error saving data to file: ${e.message}")
        }
    }

    /**
     * Đọc danh sách dữ liệu từ file JSON.
     */
    fun readDataFromFile(fileName: String): List<StatData>? {
        return try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                val json = file.readText()
                Gson().fromJson(json, Array<StatData>::class.java).toList()
            } else {
                Log.d("DataSaver", "File not found: $fileName")
                null
            }
        } catch (e: Exception) {
            Log.e("DataSaver", "Error reading data from file: ${e.message}")
            null
        }
    }

    /**
     * Đọc dữ liệu mới nhất từ file JSON.
     * @param fileName Tên file.
     * @return Phần tử mới nhất hoặc null nếu không có dữ liệu.
     */
    fun readLatestDataFromFile(fileName: String): StatData? {
        val data = readDataFromFile(fileName)
        // Nếu có dữ liệu, trả về phần tử cuối cùng (mới nhất)
        return data?.lastOrNull()
    }
}
