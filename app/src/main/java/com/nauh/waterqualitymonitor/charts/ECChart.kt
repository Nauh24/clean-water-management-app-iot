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
fun ECChart() {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context: Context -> LineChart(context) },
        update = { chart ->
            // Dữ liệu mẫu cho EC
            val entries = listOf(
                Entry(0f, 180f),  // (time, EC_value)
                Entry(1f, 220f),
                Entry(2f, 210f),
                Entry(3f, 240f),
                Entry(4f, 200f)
            )

            val dataSet = LineDataSet(entries, "EC (µS/cm)")
            dataSet.color = Color.GREEN
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
