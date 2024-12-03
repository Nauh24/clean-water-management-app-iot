package com.nauh.waterqualitymonitor.data.model

data class Alert(
//    val id: String = "", // Có thể sinh ngẫu nhiên nếu không nhận được
//    val alertType: String, // Ánh xạ từ `alert_type`
    val message: String,
    val createdAt: String = "" // Gán thời gian hiện tại nếu không có
)


