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
fun WaterBillChart() {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context: Context -> LineChart(context) },
        update = { chart ->
            // Dữ liệu mẫu cho tiền nước theo tháng
            val entries = listOf(
                Entry(0f, 100000f),  // (tháng, tiền nước)
                Entry(1f, 120000f),
                Entry(2f, 90000f),
                Entry(3f, 130000f),
                Entry(4f, 110000f)
            )

            val dataSet = LineDataSet(entries, "Water Bill (VND)")
            dataSet.color = Color.MAGENTA
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
