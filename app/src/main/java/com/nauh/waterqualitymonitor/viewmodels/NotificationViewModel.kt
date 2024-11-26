package com.nauh.waterqualitymonitor.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nauh.waterqualitymonitor.data.model.Alert
import com.nauh.waterqualitymonitor.data.model.AlertResponse
import com.nauh.waterqualitymonitor.data.network.WebSocketReceiver
import com.nauh.waterqualitymonitor.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationViewModel : ViewModel() {

    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val alerts: StateFlow<List<Alert>> = _alerts

    init {
        // Khởi tạo WebSocket ngay khi ViewModel được khởi tạo
        WebSocketReceiver.setReceiver { message ->
            handleNewAlert(message)
        }

        // Đảm bảo WebSocket luôn kết nối
        WebSocketReceiver.connect()  // Đảm bảo WebSocket luôn hoạt động

        // Lấy dữ liệu từ API ngay khi khởi động ứng dụng
        fetchAlerts()
    }

    private fun fetchAlerts() {
        // Gọi API để lấy dữ liệu thông báo ban đầu nếu cần
        RetrofitClient.apiService.getAlerts().enqueue(object : Callback<AlertResponse> {
            override fun onResponse(call: Call<AlertResponse>, response: Response<AlertResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _alerts.value = it.metadata
                            .takeLast(30) // Lấy 30 giá trị cuối cùng
//                            .reversed()   // Đảo ngược thứ tự
                    }
                } else {
                    Log.e("NotificationViewModel", "Error response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AlertResponse>, t: Throwable) {
                Log.e("NotificationViewModel", "API call failed: ${t.message}")
            }
        })
    }



    private fun handleNewAlert(message: String) {
        try {
            // Parse thông báo mới từ WebSocket và thêm vào danh sách
            val newAlert = Gson().fromJson(message, Alert::class.java)
            _alerts.value = listOf(newAlert) + _alerts.value // Thêm thông báo mới vào đầu danh sách
        } catch (e: Exception) {
            Log.e("NotificationViewModel", "Error parsing message: ${e.message}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Khi ViewModel bị hủy, ngắt kết nối WebSocket
        WebSocketReceiver.disconnect()
    }
}
