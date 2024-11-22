package com.nauh.waterqualitymonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nauh.waterqualitymonitor.data.model.StatsResponse
import com.nauh.waterqualitymonitor.data.network.RetrofitClient.apiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DashboardData(
    val turbidity: String = "0 ppm",
    val temperature: String = "0°C",
    val relayStatus: String = "Tắt"
)

class DashboardViewModel : ViewModel() {
    private val _dashboardData = MutableStateFlow(DashboardData())
    val dashboardData: StateFlow<DashboardData> = _dashboardData

    init {
        // Lấy dữ liệu ban đầu và cập nhật định kỳ
        viewModelScope.launch {
            while (true) {
                updateData()
                delay(5000) // Cập nhật mỗi 5 giây
            }
        }
    }

    private suspend fun updateData() {
        try {
            apiService.getDatas().enqueue(object : retrofit2.Callback<StatsResponse> {
                override fun onResponse(
                    call: retrofit2.Call<StatsResponse>,
                    response: retrofit2.Response<StatsResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        // Kiểm tra xem body của phản hồi có hợp lệ không
                        if (responseBody != null) {
                            val data =
                                responseBody.metadata?.data // Lấy danh sách dữ liệu từ phản hồi

                            // Kiểm tra nếu data không phải là null và có ít nhất một phần tử
                            val lastData = data?.lastOrNull()

                            if (lastData != null) {
                                // Cập nhật dữ liệu vào trạng thái
                                _dashboardData.value = DashboardData(
                                    turbidity = "${lastData.tds} ppm", // Tds (Turbidity) là độ đục
                                    temperature = "${lastData.temperature}°C",
                                    relayStatus = if (lastData.relay == 0) "Tắt" else "Bật"
                                )
                            } else {
                                // Nếu không có dữ liệu, có thể set giá trị mặc định hoặc xử lý lỗi
                                _dashboardData.value = DashboardData(
                                    turbidity = "Dữ liệu không có",
                                    temperature = "Dữ liệu không có",
                                    relayStatus = "Dữ liệu không có"
                                )
                            }
                        } else {
                            // Xử lý nếu body là null
                            _dashboardData.value = DashboardData(
                                turbidity = "Lỗi API",
                                temperature = "Lỗi API",
                                relayStatus = "Lỗi API"
                            )
                        }
                    } else {
                        // Xử lý nếu phản hồi không thành công (code lỗi không phải 200)
                        _dashboardData.value = DashboardData(
                            turbidity = "Lỗi phản hồi",
                            temperature = "Lỗi phản hồi",
                            relayStatus = "Lỗi phản hồi"
                        )
                    }
                }

                override fun onFailure(call: retrofit2.Call<StatsResponse>, t: Throwable) {
                    // Xử lý các lỗi không mong muốn trong quá trình gọi API
                    _dashboardData.value = DashboardData(
                        turbidity = "Lỗi kết nối",
                        temperature = "Lỗi kết nối",
                        relayStatus = "Lỗi kết nối"
                    )
                }
            })
        } catch (e: Exception) {
            // Xử lý các lỗi không mong muốn trong quá trình gọi API
            _dashboardData.value = DashboardData(
                turbidity = "Lỗi kết nối",
                temperature = "Lỗi kết nối",
                relayStatus = "Lỗi kết nối"
            )
        }
    }
}