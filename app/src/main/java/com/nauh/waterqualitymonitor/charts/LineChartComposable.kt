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
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun LineChartComposable(
    data: List<Pair<Float, Float>>,  // Danh sách (thời gian, giá trị đo)
    chartLabel: String,
    lineColor: Int
) {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context: Context -> LineChart(context) },
        update = { chart ->
            // Chuyển đổi dữ liệu thành Entry
            val entries = data.map { Entry(it.first, it.second) }

            val dataSet = LineDataSet(entries, chartLabel).apply {
                color = lineColor
                valueTextColor = Color.BLACK
                setCircleColor(lineColor)
                circleRadius = 4f
                lineWidth = 2f
            }

            chart.data = LineData(dataSet)

            // Cấu hình trục X
            chart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f // Khoảng cách tối thiểu giữa các nhãn
                labelRotationAngle = -45f // Xoay nhãn trục ngang
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "T${value.toInt()}" // Định dạng nhãn trục X
                    }
                }
            }

            // Cấu hình trục Y
            chart.axisLeft.apply {
                setDrawGridLines(false)
                axisMinimum = 0f // Đảm bảo giá trị tối thiểu là 0
            }
            chart.axisRight.isEnabled = false

            // Cấu hình tổng quan
            chart.description.isEnabled = false // Tắt mô tả biểu đồ
            chart.legend.isEnabled = true // Hiển thị chú thích
            chart.invalidate()
        }
    )
}
