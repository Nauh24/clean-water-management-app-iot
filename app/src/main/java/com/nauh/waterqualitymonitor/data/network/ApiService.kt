package com.nauh.waterqualitymonitor.data.network

import com.nauh.waterqualitymonitor.data.model.AlertResponse
import com.nauh.waterqualitymonitor.data.model.StatsResponse
import com.nauh.waterqualitymonitor.viewmodels.DashboardData
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/api/v1/data") // Đường dẫn từ API
    fun getDatas(): Call<StatsResponse>

    @GET("/api/v1/alert")
    fun getAlerts(): Call<AlertResponse>


}
