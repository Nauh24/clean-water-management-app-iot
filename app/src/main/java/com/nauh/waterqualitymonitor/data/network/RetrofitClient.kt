package com.nauh.waterqualitymonitor.data.network

import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
//    private const val BASE_URL = "https://iot-backend-project.onrender.com" // Base URL của API
    private const val BASE_URL = "ws://192.168.43.204:4000" // Base URL của API

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) // Đảm bảo rằng bạn đã thêm GsonConverterFactory
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    // Phương thức gửi lệnh relay đến backend
//    suspend fun sendRelayCommand(json: Map<String, String>) {
//        try {
//            // Gọi API để gửi dữ liệu relay
//            val response = apiService.sendRelayCommand(json)
//
//            // Kiểm tra phản hồi từ backend
//            if (response.isSuccessful) {
//                Log.d("RetrofitClient", "Relay command sent successfully.")
//            } else {
//                Log.e("RetrofitClient", "Failed to send relay command. Error code: ${response.code()}")
//            }
//        } catch (e: Exception) {
//            Log.e("RetrofitClient", "Error while sending relay command: ${e.message}")
//        }
//    }
    suspend fun sendRelayCommand(jsonString: String) {
        try {
            // Tạo RequestBody từ chuỗi JSON
            val requestBody = jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            // Gọi API để gửi dữ liệu relay
            val response = apiService.sendRelayCommand(requestBody)

            // Kiểm tra phản hồi từ backend
            if (response.isSuccessful) {
                Log.d("RetrofitClient", "Relay command sent successfully.")
            } else {
                Log.e("RetrofitClient", "Failed to send relay command. Error code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("RetrofitClient", "Error while sending relay command: ${e.message}")
        }
    }

}

