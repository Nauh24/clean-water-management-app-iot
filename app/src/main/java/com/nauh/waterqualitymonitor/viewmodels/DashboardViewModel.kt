package com.nauh.waterqualitymonitor.viewmodels

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.nauh.waterqualitymonitor.data.model.StatData
import com.nauh.waterqualitymonitor.utils.DataSaver
import kotlinx.coroutines.delay

class DashboardViewModel(
    private val dataSaver: DataSaver
) : ViewModel() {

    // Tạo StateFlow để lưu trữ dữ liệu Dashboard
    private val _dashboardData = MutableStateFlow(DashboardData())
    val dashboardData: StateFlow<DashboardData> = _dashboardData

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Lấy dữ liệu mới nhất từ file
            while (true) {
                // Lấy dữ liệu mới nhất từ file
//                val latestData = dataSaver.readLatestDataFromFile("data.json")
                val latestData = dataSaver.readLatestDataFromFile("stats_data_tmp.json")


                // Nếu có dữ liệu mới, cập nhật vào StateFlow
                latestData?.let {
                    _dashboardData.value = DashboardData(
                        turbidity = "${it.tds} ppm",
                        temperature = "${it.temperature}°C",
                        flowRate = "${it.flowRate} l/min",
                        relayStatus = if (it.relay == 0) "Tắt" else "Bật"
                    )
                }

                // Sau mỗi lần cập nhật, tạm dừng một chút trước khi lấy dữ liệu lại
                delay(1000) // Sử dụng delayTime được truyền vào
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