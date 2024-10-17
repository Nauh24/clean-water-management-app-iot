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
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context: Context -> LineChart(context) },
        update = { chart ->
            // Dữ liệu mẫu cho nhiệt độ
            val entries = listOf(
                Entry(0f, 25f),  // (time, temperature)
                Entry(1f, 27f),
                Entry(2f, 26f),
                Entry(3f, 28f),
                Entry(4f, 30f)
            )

            val dataSet = LineDataSet(entries, "Temperature (°C)")
            dataSet.color = Color.RED
            dataSet.valueTextColor = Color.BLACK

            val lineData = LineData(dataSet)
            chart.data = lineData

            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            chart.axisLeft.setDrawGridLines(false)
            chart.axisRight.isEnabled = false

            chart.description.isEnabled = false
            chart.invalidate()
        }
    )
}
