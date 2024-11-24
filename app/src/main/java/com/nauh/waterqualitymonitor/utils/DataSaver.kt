package com.nauh.waterqualitymonitor.utils

import android.content.Context
import android.util.Log
import androidx.compose.ui.res.stringResource
import com.google.gson.Gson
import com.nauh.waterqualitymonitor.R
import com.nauh.waterqualitymonitor.data.model.StatData
import kotlinx.coroutines.delay
import java.io.File
import java.util.Random
import java.util.UUID

class DataSaver(private val context: Context) {

    // Phương thức lấy tất cả dữ liệu từ API, sử dụng Retrofit với coroutines
    suspend fun getAllStats(): List<StatData> {
//        var currentPage = 1
//        val allData = mutableListOf<StatData>()
//
//        while (true) {
//            // Gọi API và lấy dữ liệu
//            val response = withContext(Dispatchers.IO) {
//                RetrofitClient.apiService.getDatas(page = currentPage, pageSize = 20).execute()
//            }
//
//            if (response.isSuccessful) {
//                val body = response.body()
//                if (body != null) {
//                    // Kiểm tra sự tồn tại của metadata và data
//                    val metadata = body.metadata
//                    val data = metadata?.data
//                    val meta = metadata?.meta
//
//                    // Nếu metadata hoặc data là null, dừng lại
//                    if (data != null) {
//                        // Lọc bỏ các phần tử có relay là null
//                        val filteredData = data.filter { it.relay != null }
//                        allData.addAll(filteredData)
//                        // Lấy tổng số trang từ metadata, đảm bảo có giá trị mặc định nếu không có
//                        val totalPages = meta?.totalPages ?: 1
//
//                        // Kiểm tra có còn trang sau không
//                        if (currentPage < totalPages) {
//                            currentPage++
//                        } else {
//                            break
//                        }
//                        Log.d("DataSaver", "Data size: ${allData.size}")
//                        Log.d("DataSaver", "Total pages: $totalPages")
//                        Log.d("DataSaver", "Current page: $currentPage")
//
//                    } else {
//                        break
//                    }
//                } else {
//                    // Nếu response.body() là null, dừng lại
//                    break
//                }
//            } else {
//                // Nếu có lỗi trong quá trình gọi API, dừng lại
//                break
//            }
//        }
//
//        return allData
        val random = Random()
        while (true) {
            delay(5000) // Tạm dừng 10 giây

            // Sinh dữ liệu ngẫu nhiên cho từng thuộc tính
            val temperature = random.nextInt(50) + 20 // Nhiệt độ từ 20 đến 70 độ C
            val tds = random.nextInt(1000) + 100 // TDS từ 100 đến 1100
            val flowRate = random.nextInt(500) + 1 // Flow rate từ 1 đến 500
            val createdAt = System.currentTimeMillis().toString() // Timestamp hiện tại

            // Quy tắc: Nếu nhiệt độ > 35 hoặc TDS >= 500 thì relay = 0, ngược lại relay = 1
            val relay = if (temperature > 35 || tds >= 500) 0 else 1

            // Tạo một bản ghi StatData ngẫu nhiên
            val statData = StatData(
                _id = UUID.randomUUID().toString(), // ID ngẫu nhiên
                temperature = temperature,
                tds = tds,
                flowRate = flowRate,
                relay = relay,
                createdAt = createdAt,
                __v = 0
            )

            // Lưu bản ghi ngẫu nhiên vào file hoặc xử lý theo nhu cầu
            val existingData = readDataFromFile("data.json") ?: emptyList()
            val updatedData = existingData + statData

            // Lưu lại vào file JSON
            saveDataToFile(updatedData, "data.json")

            Log.d("DataSaver", "Generated new data: $statData")
        }
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

    fun readLatestDataFromFile(fileName: String): StatData? {
        val file = File(context.filesDir, fileName)
        if (file.exists()) {
            val json = file.readText()
            val gson = Gson()
            // Giả sử dữ liệu là một danh sách các StatData
            val dataList = gson.fromJson(json, Array<StatData>::class.java).toList()

            // Nếu danh sách không rỗng, sắp xếp theo thời gian giảm dần và lấy phần tử đầu tiên (mới nhất)
            return dataList.sortedByDescending { it.createdAt }.firstOrNull()
        }
        return null
    }

}