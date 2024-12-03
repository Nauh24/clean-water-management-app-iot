package com.nauh.waterqualitymonitor.data.network

import com.nauh.waterqualitymonitor.data.model.AlertResponse
import com.nauh.waterqualitymonitor.data.model.StatsResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("/api/v1/data") // Đường dẫn từ API
    fun getDatas(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<StatsResponse>

    @GET("/api/v1/alert")
    fun getAlerts(): Call<AlertResponse>

    //    @POST("/api/v1/system")
//    suspend fun sendRelayCommand(@Body json: Map<String, String>): Response<Unit>
    @POST("/api/v1/system")
    suspend fun sendRelayCommand(@Body body: RequestBody): Response<Unit>

}
