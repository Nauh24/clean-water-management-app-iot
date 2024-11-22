package com.nauh.waterqualitymonitor.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun formatTimestamp(timestamp: String): Pair<String, String> {
    return try {
        // Chuyển timestamp thành đối tượng Date
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(timestamp)

        // Định dạng ngày
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)

        // Định dạng giờ
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedTime = timeFormat.format(date)

        formattedDate to formattedTime
    } catch (e: Exception) {
        // Trường hợp lỗi định dạng
        "Không xác định" to "Không xác định"
    }
}
