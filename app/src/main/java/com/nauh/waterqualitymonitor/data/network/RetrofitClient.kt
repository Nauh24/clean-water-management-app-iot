package com.nauh.waterqualitymonitor.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://iot-backend-project.onrender.com" // Base URL của API

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) // Đảm bảo rằng bạn đã thêm GsonConverterFactory
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

