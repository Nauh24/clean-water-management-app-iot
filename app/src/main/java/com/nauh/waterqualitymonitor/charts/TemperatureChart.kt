package com.nauh.waterqualitymonitor.charts

import android.content.Context
import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun TemperatureChart() {
    val temperatureData = listOf(
        0f to 25f, // (time, temperature)
        1f to 27f,
        2f to 26f,
        3f to 28f,
        4f to 30f
    )

    LineChartComposable(
        data = temperatureData,
        chartLabel = "Temperature (°C)",
        lineColor = Color.RED
    )
}

