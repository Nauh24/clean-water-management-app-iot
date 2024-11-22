package com.nauh.waterqualitymonitor.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nauh.waterqualitymonitor.data.model.Alert
import com.nauh.waterqualitymonitor.data.model.AlertResponse
import com.nauh.waterqualitymonitor.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationViewModel : ViewModel() {
    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val alerts: StateFlow<List<Alert>> = _alerts

    init {
        fetchAlerts()
    }

    private fun fetchAlerts() {
        RetrofitClient.apiService.getAlerts().enqueue(object : Callback<AlertResponse> {
            override fun onResponse(call: Call<AlertResponse>, response: Response<AlertResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _alerts.value = it.metadata // Lấy danh sách từ metadata

                        Log.d("NotificationViewModel", "Alerts fetched: ${it.metadata}")
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
}

