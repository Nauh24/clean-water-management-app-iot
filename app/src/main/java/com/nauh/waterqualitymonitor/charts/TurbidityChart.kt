package com.nauh.waterqualitymonitor.charts

import androidx.compose.runtime.Composable
import android.graphics.Color

@Composable
fun TurbidityChart() {
    // Dữ liệu mẫu cho độ đục (Turbidity)
    val turbidityData = listOf(
        0f to 5f, // (time, turbidity_value)
        1f to 7f,
        2f to 3f,
        3f to 8f,
        4f to 6f
    )

    LineChartComposable(
        data = turbidityData,
        chartLabel = "Turbidity (ppm)",
        lineColor = Color.BLUE
    )
}
