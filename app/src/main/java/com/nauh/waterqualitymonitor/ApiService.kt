package com.nauh.waterqualitymonitor

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/api/v1/data") // Đường dẫn từ API
    fun getDatas(): Call<StatsResponse>
}
