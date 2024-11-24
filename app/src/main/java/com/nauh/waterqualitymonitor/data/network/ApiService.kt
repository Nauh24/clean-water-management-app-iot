package com.nauh.waterqualitymonitor.data.network

import com.nauh.waterqualitymonitor.data.model.AlertResponse
import com.nauh.waterqualitymonitor.data.model.StatsResponse
import com.nauh.waterqualitymonitor.viewmodels.DashboardData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/api/v1/data") // Đường dẫn từ API
    fun getDatas(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<StatsResponse>

    @GET("/api/v1/alert")
    fun getAlerts(): Call<AlertResponse>

}
