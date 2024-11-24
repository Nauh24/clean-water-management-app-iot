package com.nauh.waterqualitymonitor.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.nauh.waterqualitymonitor.data.model.StatData
import com.nauh.waterqualitymonitor.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DataSaver(private val context: Context) {

    // Phương thức lấy tất cả dữ liệu từ API, sử dụng Retrofit với coroutines
    suspend fun getAllStats(): List<StatData> {
        var currentPage = 1
        val allData = mutableListOf<StatData>()

        while (true) {
            // Gọi API và lấy dữ liệu
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.apiService.getDatas(page = currentPage, pageSize = 20).execute()
            }

            if (response.isSuccessful) {
                Log.d("DataSaver", "API call successful")
                val body = response.body()
                if (body != null) {
                    // Kiểm tra sự tồn tại của metadata và data
                    val metadata = body.metadata
                    val data = metadata?.data
                    val meta = metadata?.meta

                    // Nếu metadata hoặc data là null, dừng lại
                    if (data != null) {
                        allData.addAll(data)
                        // Lấy tổng số trang từ metadata, đảm bảo có giá trị mặc định nếu không có
                        val totalPages = meta?.totalPages ?: 1

                        // Kiểm tra có còn trang sau không
                        if (currentPage < totalPages) {
                            currentPage++
                        } else {
                            break
                        }
                    } else {
                        break
                    }
                } else {
                    // Nếu response.body() là null, dừng lại
                    break
                }
            } else {
                // Nếu có lỗi trong quá trình gọi API, dừng lại
                break
            }
        }

        return allData
    }



    // Phương thức để sắp xếp dữ liệu theo thời gian giảm dần
    fun sortDataByTimestamp(data: List<StatData>): List<StatData> {
        return data.sortedByDescending { it.createdAt }
    }

    // Phương thức lưu dữ liệu vào file JSON
    fun saveDataToFile(data: List<StatData>, fileName: String) {
        val gson = Gson()
        val json = gson.toJson(data)
        val file = File(context.filesDir, fileName)
        file.writeText(json)
    }

    // Phương thức thực hiện toàn bộ quy trình: lấy dữ liệu, sắp xếp và lưu vào file
    suspend fun saveAllStatsToFile(fileName: String) {
        try {
            // Lấy tất cả dữ liệu từ API
            val allStats = getAllStats()

            // Sắp xếp dữ liệu theo thời gian giảm dần
            val sortedStats = sortDataByTimestamp(allStats)

            // Lưu dữ liệu đã sắp xếp vào file
            saveDataToFile(sortedStats, fileName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Phương thức đọc dữ liệu từ file (Nếu cần)
    fun readDataFromFile(fileName: String): List<StatData>? {
        val file = File(context.filesDir, fileName)
        if (file.exists()) {
            val json = file.readText()
            val gson = Gson()
            return gson.fromJson(json, Array<StatData>::class.java).toList()
        }
        return null
    }
}