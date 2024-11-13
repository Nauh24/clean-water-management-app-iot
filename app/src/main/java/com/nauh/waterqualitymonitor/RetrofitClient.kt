//package com.nauh.waterqualitymonitor
//
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//object RetrofitClient {
//    private const val BASE_URL = "https://iot-backend-project.onrender.com" // Thay bằng URL của backend
//
//    val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    val apiService: ApiService by lazy {
//        retrofit.create(ApiService::class.java)
//    }
//}
