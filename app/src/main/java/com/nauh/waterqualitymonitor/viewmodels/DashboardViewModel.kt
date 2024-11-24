package com.nauh.waterqualitymonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.nauh.waterqualitymonitor.data.model.StatData
import com.nauh.waterqualitymonitor.utils.DataSaver

class DashboardViewModel(private val dataSaver: DataSaver) : ViewModel() {

    // Tạo StateFlow để lưu trữ dữ liệu Dashboard
    private val _dashboardData = MutableStateFlow(DashboardData())
    val dashboardData: StateFlow<DashboardData> = _dashboardData

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Lấy dữ liệu từ file hoặc API (ở đây tôi dùng dữ liệu giả)
            val stats = dataSaver.readDataFromFile("stats_data.json") ?: emptyList()

            // Giả sử chúng ta có thể lấy các giá trị này từ một danh sách các `StatData`
            if (stats.isNotEmpty()) {
                val latestData = stats.first() // Lấy dữ liệu mới nhất
                _dashboardData.value = DashboardData(
                    turbidity = "${latestData.tds} ppm",
                    temperature = "${latestData.temperature}°C",
                    flowRate = "${latestData.flowRate} l/phút",
                    relayStatus = if (latestData.relay == 0) "Tắt" else "Bật"
                )
            }
        }
    }
}

data class DashboardData(
    val turbidity: String = "0 ppm",
    val temperature: String = "0°C",
    val flowRate: String = "0 l/phút",
    val relayStatus: String = "Tắt"
)